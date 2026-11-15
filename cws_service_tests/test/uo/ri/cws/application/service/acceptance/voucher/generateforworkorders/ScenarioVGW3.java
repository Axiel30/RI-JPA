package uo.ri.cws.application.service.acceptance.voucher.generateforworkorders;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGW3 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GW.3] a client with three invoiced workorders and one invoice is not paid")
	public void givenAClientWithThreeInvoicedWorkordersAndOneInvoiceIsNotPaid() {
		client = DbFixtures.aClient();
		DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 2/*paid*/, 1/*non-paid*/, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GW.3] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GW.3] no voucher is generated")
	public void thenNoVoucherIsGenerated() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 0);
	}
}
