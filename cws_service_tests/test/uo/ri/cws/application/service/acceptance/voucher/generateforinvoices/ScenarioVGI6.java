package uo.ri.cws.application.service.acceptance.voucher.generateforinvoices;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_PAID;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.USED_FOR_VOUCHER;

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

public class ScenarioVGI6 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private TInvoicesRecord newPaidInvoice;
	private TInvoicesRecord newUnpaidInvoice;

	@Given("[V.GI.6] a client with a voucher generated from a paid invoice over 500 euro")
	public void givenAClientWithAVoucherGeneratedFromAPaidInvoiceOver500Euro() {
		client = DbFixtures.aClient();
		DbFixtures.anInvoiceForClientOf(
				client.id, new BigDecimal("500.01"), PAID, USED_FOR_VOUCHER);
		DbFixtures.aVoucherForClientOf(client.id, 
				new BigDecimal("30.00"),
				new BigDecimal("30.00"), 
				"Por factura de más de 500"
			);
	}

	@And("[V.GI.6] a new paid invoice over 500 euro")
	public void andANewPaidInvoiceOver500Euro() {
		newPaidInvoice = DbFixtures.anInvoiceForClientOf(
				client.id, new BigDecimal("700.00"), PAID, NOT_USED_FOR_VOUCHER);
	}

	@And("[V.GI.6] another non-paid invoice over 500 euro")
	public void andAnotherNonPaidInvoiceOver500Euro() {
		newUnpaidInvoice = DbFixtures.anInvoiceForClientOf(
				client.id, new BigDecimal("800.00"), NOT_PAID, NOT_USED_FOR_VOUCHER);
	}

	@When("[V.GI.6] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GI.6] one new voucher of 30 euro is generated for the client")
	public void thenOneNewVoucherOf30EuroIsGeneratedForTheClient() {
		assertGenerated(0, 0, 1);
		assertVouchers(client.id, 2, "30.00");
	}

	@And("[V.GI.6] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GI.6] the invoice is marked as used for the voucher")
	public void andTheInvoiceIsMarkedAsUsedForTheVoucher() {
		assertInvoiceUsed(newPaidInvoice);
		assertInvoiceNotUsed(newUnpaidInvoice);
	}
}
