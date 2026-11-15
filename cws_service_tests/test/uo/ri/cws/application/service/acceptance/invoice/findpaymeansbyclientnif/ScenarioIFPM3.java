package uo.ri.cws.application.service.acceptance.invoice.findpaymeansbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.CashDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FPM.3] Exclude payment means owned by another client.
 */
public class ScenarioIFPM3 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TClientsRecord firstClient;
	private TPaymentMeansRecord firstCash;
	private TPaymentMeansRecord secondCash;
	private List<PaymentMeanDto> result;

	@Given("[I.FPM.3] two registered clients with payment means")
	public void givenTwoClientsWithPaymentMeans() {
		firstClient = DbFixtures.aClient();
		firstCash = DbFixtures.aCashForClient(firstClient.id);
		TClientsRecord secondClient = DbFixtures.aClient();
		secondCash = DbFixtures.aCashForClient(secondClient.id);
	}

	@When("[I.FPM.3] I search for the first client's payment means by nif")
	public void whenISearchForFirstClient() throws BusinessException {
		result = service.findPayMeansByClientNif(firstClient.nif);
	}

	@Then("[I.FPM.3] I get only payment means owned by the first client")
	public void thenIGetOnlyFirstClientPaymentMeans() {
		assertEquals(1, result.size());
		
		CashDto dto = assertInstanceOf(CashDto.class, result.get(0));
		assertEquals(firstCash.id, dto.id);
		assertNotEquals(secondCash.id, dto.id);
		assertEquals(firstCash.version, dto.version);
		assertEquals(firstClient.id, dto.clientId);
		assertEquals(0, firstCash.accumulated.compareTo(dto.accumulated));
	}
}
