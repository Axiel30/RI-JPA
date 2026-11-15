package uo.ri.cws.application.service.acceptance.paymentmean.findcreditcardbynumber;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.CreditCardNumberGenerator;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbCC2 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private Optional<CardDto> result;

	@When("[PM.FbCC.2] I search a non existing credit card by number")
	public void whenISearchANonExistingCreditCardByNumber() throws BusinessException {
		result = service.findCreditCardByNumber(CreditCardNumberGenerator.generate());
	}

	@Then("[PM.FbCC.2] no credit card is found")
	public void thenNoCreditCardIsFound() {
		assertTrue(result.isEmpty());
	}
}
