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

public class ScenarioVGW1 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TWorkOrdersRecord> workOrders;

	@Given("[V.GW.1] a client with three invoiced and paid workorders")
	public void givenAClientWithThreeInvoicedAndPaidWorkorders() {
		client = DbFixtures.aClient();
		workOrders = DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, NOT_USED_FOR_VOUCHER
			);
	}

	@When("[V.GW.1] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GW.1] one voucher of 20 euro is generated for the client")
	public void thenOneVoucherOf20EuroIsGeneratedForTheClient() {
		assertGenerated(1, 0, 0);
		assertVouchers(client.id, 1, "20.00");
	}

	@And("[V.GW.1] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GW.1] the workorders are marked as used for the voucher")
	public void andTheWorkordersAreMarkedAsUsedForTheVoucher() {
		assertUsedWorkOrders(workOrders, 3);
	}
}
