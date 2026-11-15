package uo.ri.cws.application.service.acceptance.paymentmean.findvoucherbycode;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioPMFbC2 {
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
	private Optional<VoucherDto> result;

	@When("[PM.FbC.2] I search a non existing voucher by code")
	public void whenISearchANonExistingVoucherByCode() throws BusinessException {
		result = service.findVoucherByCode("VOUCHER-" + UUID.randomUUID());
	}

	@Then("[PM.FbC.2] no voucher is found")
	public void thenNoVoucherIsFound() {
		assertTrue(result.isEmpty());
	}
}
