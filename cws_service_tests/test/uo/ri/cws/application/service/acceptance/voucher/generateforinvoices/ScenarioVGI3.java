package uo.ri.cws.application.service.acceptance.voucher.generateforinvoices;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_PAID;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;

import java.math.BigDecimal;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGI3 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private TInvoicesRecord invoice;

	@Given("[V.GI.3] a client with an unpaid invoice over 500 euro")
	public void givenAClientWithAnUnpaidInvoiceOver500Euro() {
		client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceForClientOf(
				client.id, 
				new BigDecimal("500.01"), 
				NOT_PAID, 
				NOT_USED_FOR_VOUCHER
			);
	}

	@When("[V.GI.3] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GI.3] no voucher is generated")
	public void thenNoVoucherIsGenerated() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 0);
		assertInvoiceNotUsed(invoice);
	}
}
