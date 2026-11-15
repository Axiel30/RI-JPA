package uo.ri.cws.application.service.acceptance.voucher.generateforrecommendations;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGR3 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GR.3] a client without paid invoices")
	public void givenAClientWithoutPaidInvoices() {
		client = DbFixtures.aClient();
	}

	@And("[V.GR.3] three recommended clients with paid invoices")
	public void andThreeRecommendedClientsWithPaidInvoices() {
		DbFixtures.someRecommendationsForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GR.3] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GR.3] no voucher is generated")
	public void thenNoVoucherIsGenerated() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 0);
	}
}
