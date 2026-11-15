package uo.ri.cws.application.service.acceptance.paymentmean.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CashDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbN1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private TPaymentMeansRecord cash;
	private List<PaymentMeanDto> result;

	@Given("[PM.FbN.1] a registered client with cash")
	public void givenARegisteredClientWithCash() {
		client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[PM.FbN.1] I search payment means by client nif")
	public void whenISearchPaymentMeansByClientNif() throws BusinessException {
		result = service.findPaymentMeansByClientNif(client.nif);
	}

	@Then("[PM.FbN.1] I get the cash payment mean")
	public void thenIGetTheCashPaymentMean() {
		assertEquals(1, result.size());
		PaymentMeanDto dto = result.get(0);
		assertTrue(dto instanceof CashDto);
		Asserts.dtoMatchesRecord(cash, dto);
	}
}
