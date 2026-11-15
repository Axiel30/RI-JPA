package uo.ri.cws.application.service.robustness.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenarios:
 *   - Trying to settle an invoice with a null invoice id
 *   - Trying to settle an invoice with a null charges map
 *   - Trying to settle an invoice with an empty charges map
 *   - Trying to settle an invoice with a null payment mean id
 *   - Trying to settle an invoice with a null charge amount
 */
class SettleInvoiceRobustnessTests {
	private static final String INVOICE_ID = "invoice-id";
	private static final String PAYMENT_MEAN_ID = "payment-mean-id";
	private static final BigDecimal AMOUNT = new BigDecimal("121.00");

	private final InvoicingService service = Factories.service.forCreateInvoiceService();

	/**
	 * Given a null invoice id and a valid charges map
	 * When trying to settle the invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void settleRejectsNullInvoiceId() {
		assertRejected(() -> service.settleInvoice(null, validCharges()));
	}

	/**
	 * Given a null charges map
	 * When trying to settle an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void settleRejectsNullCharges() {
		assertRejected(() -> service.settleInvoice(INVOICE_ID, null));
	}

	/**
	 * Given an empty charges map
	 * When trying to settle an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void settleRejectsEmptyCharges() {
		assertRejected(() -> service.settleInvoice(INVOICE_ID, Map.of()));
	}

	/**
	 * Given a charges map with a null payment mean id
	 * When trying to settle an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void settleRejectsNullPaymentMeanId() {
		Map<String, BigDecimal> charges = new HashMap<>(validCharges());
		charges.put(null, AMOUNT);

		assertRejected(() -> service.settleInvoice(INVOICE_ID, charges));
	}

	/**
	 * Given a charges map with a null amount
	 * When trying to settle an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void settleRejectsNullAmount() {
		Map<String, BigDecimal> charges = new HashMap<>(validCharges());
		charges.put("other-payment-mean-id", null);

		assertRejected(() -> service.settleInvoice(INVOICE_ID, charges));
	}

	private static Map<String, BigDecimal> validCharges() {
		return Map.of(PAYMENT_MEAN_ID, AMOUNT);
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
