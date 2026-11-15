package uo.ri.cws.application.service.acceptance.voucher.generateforrecommendations;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGR6 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TRecommendationsRecord> recommendations;

	@Given("[V.GR.6] a client with a paid invoice")
	public void givenAClientWithAPaidInvoice() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(client.id, new BigDecimal("100.00"), PAID, NOT_USED_FOR_VOUCHER);
	}

	@And("[V.GR.6] six recommended clients with paid invoices")
	public void andSixRecommendedClientsWithPaidInvoices() {
		recommendations = DbFixtures.someRecommendationsForClientOf(
				client.id, 6/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GR.6] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GR.6] two vouchers of 25 euro are generated for the client")
	public void thenTwoVouchersOf25EuroAreGeneratedForTheClient() {
		assertGenerated(0, 2, 0);
		assertVouchers(client.id, 2, "25.00");
	}

	@And("[V.GR.6] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GR.6] the recommendations are marked as used for the voucher")
	public void andTheRecommendationsAreMarkedAsUsedForTheVoucher() {
		assertUsedRecommendations(recommendations, 6);
	}
}
