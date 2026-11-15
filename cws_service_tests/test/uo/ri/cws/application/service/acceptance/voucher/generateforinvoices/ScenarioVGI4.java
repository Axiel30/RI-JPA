package uo.ri.cws.application.service.acceptance.voucher.generateforinvoices;

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

public class ScenarioVGI4 extends AbstractVoucherScenario {
	private TClientsRecord client;

	@Given("[V.GI.4] a client with a voucher generated from a paid invoice over 500 euro")
	public void givenAClientWithAVoucherGeneratedFromAPaidInvoiceOver500Euro() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(client.id, new BigDecimal("500.01"), PAID, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, 
				new BigDecimal("30.00"),
				new BigDecimal("30.00"), 
				"Por factura de más de 500"
			);
	}

	@When("[V.GI.4] I generate vouchers again")
	public void whenIGenerateVouchersAgain() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GI.4] no new invoice voucher is generated for the client")
	public void thenNoNewInvoiceVoucherIsGeneratedForTheClient() {
		assertGenerated(0, 0, 0);
		assertVoucherCount(client.id, 1);
	}
}
