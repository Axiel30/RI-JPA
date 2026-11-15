package uo.ri.cws.application.service.acceptance.voucher.findbyclientnif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioVFbN2 {
	private final VoucherGenerationService service = Factories.service.forVoucherService();
	private TClientsRecord client;
	private List<VoucherDto> result;

	@Given("[V.FbN.2] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[V.FbN.2] I search vouchers by client nif")
	public void whenISearchVouchersByClientNif() throws BusinessException {
		result = service.findVouchersByClientNif(client.nif);
	}

	@Then("[V.FbN.2] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
