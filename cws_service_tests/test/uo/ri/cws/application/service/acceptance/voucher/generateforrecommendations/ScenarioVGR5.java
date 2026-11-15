package uo.ri.cws.application.service.acceptance.voucher.generateforrecommendations;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.USED_FOR_VOUCHER;

import java.math.BigDecimal;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGR5 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GR.5] a client with a voucher generated from three recommendations")
	public void givenAClientWithAVoucherGeneratedFromThreeRecommendations() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(client.id, new BigDecimal("100.00"), PAID, NOT_USED_FOR_VOUCHER);
		DbFixtures.someRecommendationsForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("25.00"),
				new BigDecimal("25.00"), "Por recomendación");
	}

	@When("[V.GR.5] I generate vouchers again")
	public void whenIGenerateVouchersAgain() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GR.5] no new recommendation voucher is generated for the client")
	public void thenNoNewRecommendationVoucherIsGeneratedForTheClient() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 1);
	}
}
