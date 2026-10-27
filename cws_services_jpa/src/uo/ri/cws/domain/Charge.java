package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Charge {

	// Natural attributes
    private Money amount; // cents

    // Accidental attributes
    private Invoice invoice;
    private PaymentMean paymentMean;

    // ------ Constructors -----
    
    /* package */ Charge(Invoice invoice, PaymentMean paymentMean, Money amount) {
    	ArgumentChecks.isNotNull(invoice, "Invoice cannot be null");
    	ArgumentChecks.isNotNull(paymentMean, "Payment mean cannot be null");
    	ArgumentChecks.isNotNull(amount, "Amount cannot be null");
    	Money normalized = Money.calc(amount).toMoneyRoundedToCents();
    	ArgumentChecks.isTrue(normalized.greaterThan(Money.ZERO), "Amount must be positive");

    	this.amount = normalized;
    	paymentMean.charge(normalized);
    	Associations.SettlesWithdraws.atomicLink(invoice, this, paymentMean);
    }

    // ---- Getters ----

    public Money getAmount() {
        return amount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public PaymentMean getPaymentMean() {
        return paymentMean;
    }

    // ---- Bussiness methods  ----

    /**
     * Restores the value to the payment mean and unlinks the charge from the invoice and the payment mean.
     */
	/* package */ void rewind() {
		StateChecks.isTrue(invoice != null, "Charge already rewound");
		StateChecks.isTrue(paymentMean != null, "Charge already rewound");
		paymentMean.rewind(amount);
		Associations.SettlesWithdraws.atomicUnlink(this);
    }

    // ---- Association helpers ----

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    void _setPaymentMean(PaymentMean paymentMean) {
        this.paymentMean = paymentMean;
    }

    // ---- toString ----
    @Override
    public String toString() {
		return "Charge [invoice=" + invoice + ", paymentMean=" + paymentMean + ", amount=" + amount + "]";
    }
}
