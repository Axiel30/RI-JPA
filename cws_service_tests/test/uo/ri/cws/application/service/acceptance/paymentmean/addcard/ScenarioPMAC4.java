package uo.ri.cws.application.service.acceptance.paymentmean.addcard;

import java.time.YearMonth;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.CardDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;

public class ScenarioPMAC4 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private TClientsRecord client;

	@Given("[PM.AC.4] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[PM.AC.4] I try to add an outdated credit card for that client")
	public void whenITryToAddAnOutdatedCreditCardForThatClient() {
		CardDto card = new CardDtoBuilder()
				.withClientId(client.id)
				.withCardExpiration(YearMonth.now().minusMonths(1))
				.build();
		context.tryAndKeep(() -> service.addCard(card));
	}

	@Then("[PM.AC.4] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
