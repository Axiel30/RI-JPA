package uo.ri.cws.application.service.acceptance.paymentmean.findbyid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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

public class ScenarioPMFbI1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord cash;
	private Optional<PaymentMeanDto> result;

	@Given("[PM.FbI.1] a registered client with cash")
	public void givenARegisteredClientWithCash() {
		TClientsRecord client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[PM.FbI.1] I search the payment mean by id")
	public void whenISearchThePaymentMeanById() throws BusinessException {
		result = service.findById(cash.id);
	}

	@Then("[PM.FbI.1] I get the cash payment mean")
	public void thenIGetTheCashPaymentMean() {
		assertTrue(result.isPresent());
		PaymentMeanDto dto = result.get();
		assertTrue(dto instanceof CashDto);
		Asserts.dtoMatchesRecord(cash, dto);
	}
}
