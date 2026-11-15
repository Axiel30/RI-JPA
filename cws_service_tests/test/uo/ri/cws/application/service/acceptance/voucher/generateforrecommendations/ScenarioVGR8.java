package uo.ri.cws.application.service.acceptance.voucher.generateforrecommendations;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.USED_FOR_VOUCHER;

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

public class ScenarioVGR8 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TRecommendationsRecord> newRecommendations;

	@Given("[V.GR.8] a client with a voucher generated from three recommendations")
	public void givenAClientWithAVoucherGeneratedFromThreeRecommendations() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(client.id, new BigDecimal("100.00"), PAID, NOT_USED_FOR_VOUCHER);
		DbFixtures.someRecommendationsForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("25.00"),
				new BigDecimal("25.00"), "Por recomendación");
	}

	@And("[V.GR.8] four new recommended clients with paid invoices")
	public void andFourNewRecommendedClientsWithPaidInvoices() {
		newRecommendations = DbFixtures.someRecommendationsForClientOf(
				client.id, 4/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@And("[V.GR.8] two of the new recommended clients have no paid invoices")
	public void andTwoOfTheNewRecommendedClientsHaveNoPaidInvoices() {
		newRecommendations.addAll(
				DbFixtures.someRecommendationsForClientOf(
						client.id, 0/*paid*/, 2/*non-paid*/, NOT_USED_FOR_VOUCHER));
	}

	@When("[V.GR.8] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GR.8] one new voucher of 25 euro is generated for the client")
	public void thenOneNewVoucherOf25EuroIsGeneratedForTheClient() {
		assertGenerated(0, 1, 0);
		assertVouchers(client.id, 2, "25.00");
	}

	@And("[V.GR.8] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GR.8] the recommendations are marked as used for the voucher")
	public void andTheRecommendationsAreMarkedAsUsedForTheVoucher() {
		assertUsedRecommendations(newRecommendations, 3);
	}
}
