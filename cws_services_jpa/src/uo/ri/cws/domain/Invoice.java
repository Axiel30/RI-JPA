package uo.ri.cws.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Invoice {
	private static final LocalDate JULY_1_2012 = LocalDate.of(2012, 7, 1);

	public enum InvoiceState {
		ISSUED, PAID
	}

	// Natural attributes
	private long number;
	private LocalDate date;
	private InvoiceState state = InvoiceState.ISSUED;

	/*
	 * Subtotal amount of the invoice, before applying VAT. This represents the taxable base.
	 */
	private Money subtotal = Money.ZERO;  // cents

	/*
	 * VAT rate applied to the invoice, expressed as a percentage. Example: 21% VAT.
	 */
	private Percentage vatRate = Percentage.ofPercent(21);  // default to 21%

	/*
	 * VAT amount calculated from the subtotal amount. This is the tax the customer must pay.
	 */
	private Money vatAmount = Money.ZERO;  // cents

	/*
	 * Total amount of the invoice including VAT. This is the final amount the customer must pay.
	 */
	private Money total = Money.ZERO; // cents

	private boolean usedForVoucher = false;

	// Accidental attributes
	private Set<WorkOrder> workOrders = new HashSet<>();
	private Set<Charge> charges = new HashSet<>();

	// ----------------- Constructors -------------------------------------
	
	public Invoice(long number) {
		this(number, LocalDate.now(), List.of());
	}

	public Invoice(long number, LocalDate date) {
		this(number, date, List.of());
	}

	public Invoice(long number, List<WorkOrder> workOrders) {
		this(number, LocalDate.now(), workOrders);
	}

	public Invoice(long number, LocalDate date, List<WorkOrder> workOrders) {
		ArgumentChecks.isNotNull(date, "Date must not be null");
		ArgumentChecks.isNotNull(workOrders, "Work orders list must not be null");
		for (WorkOrder wo : workOrders) {
			ArgumentChecks.isNotNull(wo, "Work order cannot be null");
			StateChecks.isTrue(wo.isApproved(), "Work order must be approved");
		}
		this.number = number;
		this.date = date;
		this.vatRate = getApplicableVatRate();
		workOrders.forEach(this::addWorkOrder);
	}

	// ---- Getters ----
	public long getNumber() {
		return number;
	}

	public LocalDate getDate() {
		return date;
	}

	public InvoiceState getState() {
		return state;
	}

	public Money getSubtotal() {
		return subtotal;
	}

	public Percentage getVatRate() {
		return vatRate;
	}

	public Percentage getApplicableVatRate() {
		return date.isAfter(JULY_1_2012) ? Percentage.ofPercent(21) : Percentage.ofPercent(18);
	}

	public Money getVatAmount() {
		return vatAmount;
	}

	public Money getTotal() {
		return total;
	}

	public boolean isUsedForVoucher() {
		return usedForVoucher;
	}

	public void markAsUsedForVoucher() {
		this.usedForVoucher = true;
	}

	public boolean canGenerateVoucher30() {
		return isSettled() && !isUsedForVoucher() && total.greaterThan(Money.of("500.00"));
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	public Set<Charge> getCharges() {
		return new HashSet<>(charges);
	}

	// ------------------ Association helpers (package-private) --------------------------
	
	Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	Set<Charge> _getCharges() {
		return charges;
	}

	// ---- Business methods --------------------------------------------
	
	/**
	 * Adds a work order to the invoice. The work order must be approved and the invoice must not be settled.
	 * @param workOrder
	 */
	public void addWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "Work order cannot be null");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");
		StateChecks.isTrue(workOrder.isApproved(), "Work order must be approved");
		Associations.Bills.link(this, workOrder);
		workOrder.invoice();
		computeAmount();
	}

	/**
	 * Removes a work order from the invoice. The invoice must not be settled.
	 * @param workOrder
	 */
	public void removeWorkOrder(WorkOrder workOrder) {
		ArgumentChecks.isNotNull(workOrder, "Work order cannot be null");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");
		ArgumentChecks.isTrue(workOrders.contains(workOrder), "Work order not in invoice");
		workOrder.uninvoice();
		Associations.Bills.unlink(workOrder);
		computeAmount();
	}

	public boolean isSettled() {
		return state == InvoiceState.PAID;
	}

	/**
	 * Settles the invoice by charging the specified payment means with the requested amounts.
	 * The total of the charges must equal the invoice total, and each payment mean must be able to be charged.
	 * @param requestedCharges A map of payment means and their corresponding amounts to charge.
	 */
	public void settle(Map<PaymentMean, Money> requestedCharges) {
		ArgumentChecks.isNotNull(requestedCharges, "Requested charges cannot be null");
		ArgumentChecks.isFalse(requestedCharges.isEmpty(), "Requested charges cannot be empty");
		StateChecks.isFalse(isSettled(), "Invoice is already settled");
		StateChecks.isTrue(total.greaterThan(Money.ZERO), "Zero-total invoice cannot be settled");

		Map<PaymentMean, Money> normalized = normalize(requestedCharges);
		Money chargesSum = chargesTotal(normalized);
		StateChecks.isTrue(total.equals(chargesSum), "Charges total must match invoice total");

		for (Map.Entry<PaymentMean, Money> entry : normalized.entrySet()) {
			StateChecks.isTrue(entry.getKey().canBeCharged(entry.getValue()), "Payment mean cannot accept charge");
		}

		for (Map.Entry<PaymentMean, Money> entry : normalized.entrySet()) {
			new Charge(this, entry.getKey(), entry.getValue());
		}

		this.state = InvoiceState.PAID;
	}

	public void unSettle() {
		StateChecks.isTrue(isSettled(), "Invoice is not settled");

		new HashSet<>(charges).forEach(Charge::rewind);
		state = InvoiceState.ISSUED;
	}

	// ---- Calculation ----
	private void computeAmount() {
		Money.MoneyCalculator calc = Money.calc();
		for (WorkOrder wo : workOrders) {
			calc.add(wo.getAmount());
		}
		this.subtotal = calc.toMoneyRoundedToCents();
		this.vatRate = getApplicableVatRate();
		this.vatAmount = Money.calc(this.subtotal).times(this.vatRate.asRatio()).toMoneyRoundedToCents();
		this.total = Money.calc(this.subtotal).add(this.vatAmount).toMoneyRoundedToCents();
	}

	private Map<PaymentMean, Money> normalize(Map<PaymentMean, Money> requestedCharges) {
		Map<PaymentMean, Money> result = new LinkedHashMap<>();
		requestedCharges.forEach((paymentMean, amount) -> {
			ArgumentChecks.isNotNull(paymentMean, "Payment mean cannot be null");
			ArgumentChecks.isNotNull(amount, "Charge amount cannot be null");
			Money normalizedAmount = Money.calc(amount).toMoneyRoundedToCents();
			ArgumentChecks.isTrue(normalizedAmount.greaterThan(Money.ZERO), "Charge amount must be positive");
			result.put(paymentMean, normalizedAmount);
		});
		return result;
	}

	private Money chargesTotal(Map<PaymentMean, Money> requestedCharges) {
		Money.MoneyCalculator calculator = Money.calc();
		requestedCharges.values().forEach(calculator::add);
		return calculator.toMoneyRoundedToCents();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Invoice other = (Invoice) obj;
		return number == other.number;
	}

	@Override
	public int hashCode() {
		return Objects.hash(number);
	}

	@Override
	public String toString() {
		return "Invoice [number=" + number + ", date=" + date + ", state=" + state + ", total=" + total + "]";
	}

}
