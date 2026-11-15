package uo.ri.ui.cashier.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.UserInteractionChecks;
import uo.ri.util.menu.Action;

public class SettleInvoiceAction implements Action {

	private InvoicingService cs = Factories.service.forCreateInvoiceService();

	@Override
	public void execute() throws Exception {
		Long number = Console.readLong("Invoice number");
		Optional<InvoiceDto> oi = cs.findInvoiceByNumber(number);
		UserInteractionChecks.exists( oi, "There is no such invoice");
		InvoiceDto invoice = oi.get();

		Printer.printInvoice( invoice );

		String nif = Console.readString("Client nif");
		List<PaymentMeanDto> means = cs.findPayMeansByClientNif(nif);
		UserInteractionChecks.isFalse( means.isEmpty(),
				"The client has no payment means or the nif is wrong"
			);

		Map<String, BigDecimal> charges = askForCharges(means, invoice.total);

		cs.settleInvoice(invoice.id, charges);

		Console.println("The invoice has been settled");
	}

	private Map<String, BigDecimal> askForCharges(List<PaymentMeanDto> means, BigDecimal totalAmount) {
		Map<String, BigDecimal> res = new HashMap<>();

		do {
			showPaymentMeans(means);
			String id = askForPaymentMeanId();
			BigDecimal amount = askForAmount();
			res.put(id, amount);

		} while ( totalAmount.compareTo(sumAmounts( res.values() )) > 0); 
				
		return res;
	}

	private BigDecimal sumAmounts(Collection<BigDecimal> amounts) {
		return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal askForAmount() {
		return Console.readBigDecimal("Amount to charge");
	}

	private String askForPaymentMeanId() {
		return Console.readString("Payment mean id");
	}

	private void showPaymentMeans(List<PaymentMeanDto> means) {
		Printer.printPaymentMeans( means );
	}

}
