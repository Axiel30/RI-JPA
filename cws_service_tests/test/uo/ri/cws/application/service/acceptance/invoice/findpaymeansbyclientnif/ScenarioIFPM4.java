package uo.ri.cws.application.service.acceptance.invoice.findpaymeansbyclientnif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FPM.4] Find a client without payment means.
 */
public class ScenarioIFPM4 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TClientsRecord client;
	private List<PaymentMeanDto> result;

	@Given("[I.FPM.4] a registered client without payment means")
	public void givenClientWithoutPaymentMeans() {
		client = DbFixtures.aClient();
	}

	@When("[I.FPM.4] I search for payment means by the client's nif")
	public void whenISearchByClientNif() throws BusinessException {
		result = service.findPayMeansByClientNif(client.nif);
	}

	@Then("[I.FPM.4] I get an empty list")
	public void thenListIsEmpty() {
		assertTrue(result.isEmpty());
	}
}
