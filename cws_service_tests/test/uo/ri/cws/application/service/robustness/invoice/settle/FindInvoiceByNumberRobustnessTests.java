package uo.ri.cws.application.service.robustness.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenarios:
 *   - Trying to find an invoice with a null number
 */
class FindInvoiceByNumberRobustnessTests {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();

	/**
	 * Given a null invoice number
	 * When trying to find the invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void findInvoiceByNumberRejectsNullNumber() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.findInvoiceByNumber(null));

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
