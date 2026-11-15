package uo.ri.cws.application.service.acceptance.paymentmean.addcard;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import uo.ri.cws.application.service.acceptance.util.dtobuilders.CardDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMAC1 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private CardDto result;

	@Given("[PM.AC.1] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[PM.AC.1] I add a new credit card for that client")
	public void whenIAddANewCreditCardForThatClient() throws BusinessException {
		CardDto card = new CardDtoBuilder()
				.withClientId(client.id)
				.build();
		result = service.addCard(card);
	}

	@Then("[PM.AC.1] the credit card results added to the system")
	public void thenTheCreditCardResultsAddedToTheSystem() {
		assertNotNull(result.id);
		TPaymentMeansRecord paymentMean = Db.findById(TPaymentMeansRecord.class, result.id);
		TCreditCardsRecord card = Db.findById(TCreditCardsRecord.class, result.id);
		assertNotNull(paymentMean);
		assertNotNull(card);
		Asserts.dtoMatchesRecords(paymentMean, card, result);
	}
}
