package uo.ri.cws.application.service.acceptance.invoice.findbynumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FbN.2] Find an invoice with a non-existing number.
 */
public class ScenarioIFbN2 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private Optional<InvoiceDto> result;

	@When("[I.FbN.2] I search for an invoice with a non-existing number")
	public void whenISearchForMissingNumber() throws BusinessException {
		result = service.findInvoiceByNumber(Long.MAX_VALUE);
	}

	@Then("[I.FbN.2] I get an empty result")
	public void thenResultIsEmpty() {
		assertTrue(result.isEmpty());
	}
}
