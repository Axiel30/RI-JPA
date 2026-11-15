package uo.ri.cws.application.service.acceptance.voucher.generateforworkorders;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.USED_FOR_VOUCHER;

import java.math.BigDecimal;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGW4 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GW.4] a client with a voucher generated from three paid workorders")
	public void givenAClientWithAVoucherGeneratedFromThreePaidWorkorders() {
		client = DbFixtures.aClient();
		DbFixtures.someWorkOrdersInvoicedForClientOf(
				client.id, 3/*paid*/, 0/*non-paid*/, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, 
				new BigDecimal("20.00"),
				new BigDecimal("20.00"), 
				"Por tres órdenes de trabajo"
			);
	}

	@When("[V.GW.4] I generate vouchers again")
	public void whenIGenerateVouchersAgain() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GW.4] no new workorder voucher is generated for the client")
	public void thenNoNewWorkorderVoucherIsGeneratedForTheClient() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 1);
	}
}
