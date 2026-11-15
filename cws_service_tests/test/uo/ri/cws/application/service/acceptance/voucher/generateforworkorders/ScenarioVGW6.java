package uo.ri.cws.application.service.acceptance.voucher.generateforworkorders;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.USED_FOR_VOUCHER;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGW6 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TWorkOrdersRecord> newWorkOrders;

	@Given("[V.GW.6] a client with a voucher generated from three paid workorders")
	public void givenAClientWithAVoucherGeneratedFromThreePaidWorkorders() {
		client = DbFixtures.aClient();
		DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, new BigDecimal("20.00"),
				new BigDecimal("20.00"), "Por tres órdenes de trabajo");
	}

	@And("[V.GW.6] six new invoiced workorders and only five of them are paid")
	public void andSixNewInvoicedWorkordersAndOnlyFiveOfThemArePaid() {
		newWorkOrders = DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 5/*paid*/, 1/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GW.6] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GW.6] one new voucher of 20 euro is generated for the client")
	public void thenOneNewVoucherOf20EuroIsGeneratedForTheClient() {
		assertGenerated(1, 0, 0);
		assertVouchers(client.id, 2, "20.00");
	}

	@And("[V.GW.6] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GW.6] the workorders are marked as used for the voucher")
	public void andTheWorkordersAreMarkedAsUsedForTheVoucher() {
		assertUsedWorkOrders(newWorkOrders, 3);
	}
}
