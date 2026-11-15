package uo.ri.cws.application.service.acceptance.voucher.generateforrecommendations;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;

import java.math.BigDecimal;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGR2 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GR.2] a client with a paid invoice")
	public void givenAClientWithAPaidInvoice() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(client.id, new BigDecimal("100.00"), PAID, NOT_USED_FOR_VOUCHER);
	}

	@And("[V.GR.2] two recommended clients with paid invoices")
	public void andTwoRecommendedClientsWithPaidInvoices() {
		DbFixtures.someRecommendationsForClientOf(
				client.id, 2/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GR.2] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GR.2] no voucher is generated")
	public void thenNoVoucherIsGenerated() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 0);
	}
}
