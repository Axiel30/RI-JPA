package uo.ri.cws.application.service.acceptance.voucher.generateforinvoices;

import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.NOT_USED_FOR_VOUCHER;
import static uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures.PAID;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.voucher.AbstractVoucherScenario;
import uo.ri.util.exception.BusinessException;

public class ScenarioVGI5 extends AbstractVoucherScenario {
	private TClientsRecord client;
	private List<TInvoicesRecord> invoices;

	@Given("[V.GI.5] a client with two paid invoices over 500 euro")
	public void givenAClientWithTwoPaidInvoicesOver500Euro() {
		client = DbFixtures.aClient();
		invoices = List.of(
				DbFixtures.anInvoiceForClientOf(
						client.id, new BigDecimal("500.01"), PAID, NOT_USED_FOR_VOUCHER),
				DbFixtures.anInvoiceForClientOf(
						client.id, new BigDecimal("750.00"), PAID, NOT_USED_FOR_VOUCHER)
			);
	}

	@When("[V.GI.5] I generate vouchers")
	public void whenIGenerateVouchers() throws BusinessException {
		generateVouchers();
	}

	@Then("[V.GI.5] two vouchers of 30 euro are generated for the client")
	public void thenTwoVouchersOf30EuroAreGeneratedForTheClient() {
		assertGenerated(0, 0, 2);
		assertVouchers(client.id, 2, "30.00");
	}

	@And("[V.GI.5] the voucher description is {string}")
	public void andTheVoucherDescriptionIs(String description) {
		assertVoucherDescriptions(client.id, description);
	}

	@And("[V.GI.5] the invoices are marked as used for the voucher")
	public void andTheInvoicesAreMarkedAsUsedForTheVoucher() {
		invoices.forEach(this::assertInvoiceUsed);
	}
}
