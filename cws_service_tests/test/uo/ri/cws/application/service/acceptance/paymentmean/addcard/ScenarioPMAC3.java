package uo.ri.cws.application.service.acceptance.paymentmean.addcard;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCreditCardsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.CardDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;

public class ScenarioPMAC3 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;
	private TCreditCardsRecord existingCard;

	@Given("[PM.AC.3] a registered client with a credit card")
	public void givenARegisteredClientWithACreditCard() {
		client = DbFixtures.aClient();
		TPaymentMeansRecord paymentMean = DbFixtures.aCreditCardForClient(client.id);
		existingCard = Db.findById(TCreditCardsRecord.class, paymentMean.id);
	}

	@When("[PM.AC.3] I try to add another credit card with same number")
	public void whenITryToAddAnotherCreditCardWithSameNumber() {
		CardDto card = new CardDtoBuilder()
				.withClientId(client.id)
				.withCardNumber(existingCard.number)
				.build();
		context.tryAndKeep(() -> service.addCard(card));
	}

	@Then("[PM.AC.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
