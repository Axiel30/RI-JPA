package uo.ri.cws.application.service.acceptance.paymentmean.findbyid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbI4 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private Optional<PaymentMeanDto> result;

	@When("[PM.FbI.4] I search a non existing payment mean by id")
	public void whenISearchANonExistingPaymentMeanById() throws BusinessException {
		result = service.findById(UUID.randomUUID().toString());
	}

	@Then("[PM.FbI.4] no payment mean is found")
	public void thenNoPaymentMeanIsFound() {
		assertTrue(result.isEmpty());
	}
}
