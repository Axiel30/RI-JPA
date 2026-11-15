package uo.ri.cws.application.service.robustness.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenarios:
 *   - Trying to find payment means with a null, blank or malformed client NIF
 */
class FindPayMeansByClientNifRobustnessTests {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();

	/**
	 * Given a null, blank or malformed client NIF
	 * When trying to find the client's payment means
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   ", "not-a-nif", "12345678A" })
	void findPayMeansByClientNifRejectsInvalidNif(String nif) {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> service.findPayMeansByClientNif(nif));

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
