package uo.ri.cws.application.service.acceptance.invoice.findpaymeansbyclientnif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FPM.5] Find payment means for a non-existing client.
 */
public class ScenarioIFPM5 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private List<PaymentMeanDto> result;

	@When("[I.FPM.5] I search for payment means by a non-existing client nif")
	public void whenISearchForMissingClient() throws BusinessException {
		String missingNif = NifGenerator.generate();
		result = service.findPayMeansByClientNif(missingNif);
	}

	@Then("[I.FPM.5] I get an empty list")
	public void thenListIsEmpty() {
		assertTrue(result.isEmpty());
	}
}
