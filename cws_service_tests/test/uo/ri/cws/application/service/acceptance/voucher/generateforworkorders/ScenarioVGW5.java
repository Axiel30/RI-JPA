package uo.ri.cws.application.service.acceptance.voucher.generateforworkorders;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;

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

public class ScenarioVGW5 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TWorkOrdersRecord> workOrders;

	@Given("[V.GW.5] a client with six invoiced and paid workorders")
	public void givenAClientWithSixInvoicedAndPaidWorkorders() {
		client = DbFixtures.aClient();
		workOrders = DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 6/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GW.5] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GW.5] two vouchers of 20 euro are generated for the client")
	public void thenTwoVouchersOf20EuroAreGeneratedForTheClient() {
		assertGenerated(2, 0, 0);
		assertVouchers(client.id, 2, "20.00");
	}

	@And("[V.GW.5] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GW.5] the workorders are marked as used for the voucher")
	public void andTheWorkordersAreMarkedAsUsedForTheVoucher() {
		assertUsedWorkOrders(workOrders, 6);
	}
}
