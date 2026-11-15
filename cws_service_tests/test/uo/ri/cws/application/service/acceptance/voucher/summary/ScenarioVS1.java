package uo.ri.cws.application.service.acceptance.voucher.summary;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioVS1 {
	private final VoucherGenerationService service = Factories.service.forVoucherService();
	private List<VoucherSummaryDto> result;

	@Given("[V.S.1] there are no vouchers")
	public void givenThereAreNoVouchers() {
	}

	@When("[V.S.1] I ask for the voucher summary")
	public void whenIAskForTheVoucherSummary() throws BusinessException {
		result = service.getVoucherSummary();
	}

	@Then("[V.S.1] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
