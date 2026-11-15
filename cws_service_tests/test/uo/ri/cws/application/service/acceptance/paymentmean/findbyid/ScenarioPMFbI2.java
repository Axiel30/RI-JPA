package uo.ri.cws.application.service.acceptance.paymentmean.findbyid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbI2 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;
	private TCreditCardsRecord card;
	private Optional<PaymentMeanDto> result;

	@Given("[PM.FbI.2] a registered client with a credit card")
	public void givenARegisteredClientWithACreditCard() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aCreditCardForClient(client.id);
		card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
	}

	@When("[PM.FbI.2] I search the payment mean by id")
	public void whenISearchThePaymentMeanById() throws BusinessException {
		result = service.findById(paymentMean.id);
	}

	@Then("[PM.FbI.2] I get the credit card")
	public void thenIGetTheCreditCard() {
		assertTrue(result.isPresent());
		PaymentMeanDto dto = result.get();
		assertTrue(dto instanceof CardDto);
		Asserts.dtoMatchesRecords(paymentMean, card, (CardDto) dto);
	}
}
