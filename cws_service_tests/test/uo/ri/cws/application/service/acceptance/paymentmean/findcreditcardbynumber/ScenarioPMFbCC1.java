package uo.ri.cws.application.service.acceptance.paymentmean.findcreditcardbynumber;

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
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbCC1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TPaymentMeansRecord paymentMean;
	private TCreditCardsRecord card;
	private Optional<CardDto> result;

	@Given("[PM.FbCC.1] a registered client with a credit card")
	public void givenARegisteredClientWithACreditCard() {
		TClientsRecord client = DbFixtures.aClient();
		paymentMean = DbFixtures.aCreditCardForClient(client.id);
		card = Db.findById(TCreditCardsRecord.class, paymentMean.id);
	}

	@When("[PM.FbCC.1] I search the credit card by number")
	public void whenISearchTheCreditCardByNumber() throws BusinessException {
		result = service.findCreditCardByNumber(card.number);
	}

	@Then("[PM.FbCC.1] I get the credit card")
	public void thenIGetTheCreditCard() {
		assertTrue(result.isPresent());
		CardDto dto = result.get();
		Asserts.dtoMatchesRecords(paymentMean, card, dto);
	}
}
