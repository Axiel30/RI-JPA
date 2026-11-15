package uo.ri.cws.application.service.acceptance.voucher.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioVFbN3 {
	private final VoucherGenerationService service = Factories.service.forVoucherService();
	private List<VoucherDto> result;

	@When("[V.FbN.3] I search vouchers for a non existing client nif")
	public void whenISearchVouchersForANonExistingClientNif() throws BusinessException {
		result = service.findVouchersByClientNif(NifGenerator.generate());
	}

	@Then("[V.FbN.3] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
