package uo.ri.cws.application.service.acceptance.invoice.findpaymeansbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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
 * Scenario: [I.FPM.1] Find a client's cash payment mean.
 */
public class ScenarioIFPM1 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TClientsRecord client;
	private TPaymentMeansRecord cash;
	private List<PaymentMeanDto> result;

	@Given("[I.FPM.1] a registered client with a cash payment mean")
	public void givenClientWithCash() {
		client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[I.FPM.1] I search for payment means by the client's nif")
	public void whenISearchByClientNif() throws BusinessException {
		result = service.findPayMeansByClientNif(client.nif);
	}

	@Then("[I.FPM.1] I get one CashDto with its id, version, client id and accumulated amount")
	public void thenIGetTheCashPaymentMean() {
		assertEquals(1, result.size());
		CashDto dto = assertInstanceOf(CashDto.class, result.get(0));
		assertEquals(cash.id, dto.id);
		assertEquals(cash.version, dto.version);
		assertEquals(client.id, dto.clientId);
		assertEquals(0, cash.accumulated.compareTo(dto.accumulated));
	}
}
