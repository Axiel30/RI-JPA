package uo.ri.cws.application.service.acceptance.paymentmean.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbN3 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private List<PaymentMeanDto> result;

	@When("[PM.FbN.3] I search payment means for a non existing client nif")
	public void whenISearchPaymentMeansForANonExistingClientNif() throws BusinessException {
		result = service.findPaymentMeansByClientNif(NifGenerator.generate());
	}

	@Then("[PM.FbN.3] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
