package uo.ri.cws.application.service.acceptance.voucher.generateforinvoices;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;

import java.math.BigDecimal;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGI1 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private TInvoicesRecord invoice;

	@Given("[V.GI.1] a client with a paid invoice over 500 euro")
	public void givenAClientWithAPaidInvoiceOver500Euro() {
		client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceForClientOf(
				client.id, 
				new BigDecimal("500.01"), 
				PAID, 
				NOT_USED_FOR_VOUCHER
			);
	}

	@When("[V.GI.1] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GI.1] one voucher of 30 euro is generated for the client")
	public void thenOneVoucherOf30EuroIsGeneratedForTheClient() {
		assertGenerated(0, 0, 1);
		assertVouchers(client.id, 1, "30.00");
	}

	@And("[V.GI.1] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GI.1] the invoice is marked as used for the voucher")
	public void andTheInvoiceIsMarkedAsUsedForTheVoucher() {
		assertInvoiceUsed(invoice);
	}
}
