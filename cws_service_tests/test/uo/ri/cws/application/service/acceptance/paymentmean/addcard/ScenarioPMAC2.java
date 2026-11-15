package uo.ri.cws.application.service.acceptance.paymentmean.addcard;

import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.CardDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;

public class ScenarioPMAC2 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();

	@When("[PM.AC.2] I try to add a credit card for a non existing client")
	public void whenITryToAddACreditCardForANonExistingClient() {
		CardDto card = new CardDtoBuilder()
				.withClientId(UUID.randomUUID().toString())
				.build();
		context.tryAndKeep(() -> service.addCard(card));
	}

	@Then("[PM.AC.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
