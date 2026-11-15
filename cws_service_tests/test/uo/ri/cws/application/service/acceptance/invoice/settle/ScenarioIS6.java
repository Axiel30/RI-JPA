package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TChargesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenario: [I.S.6] Reject settlement of an invoice already paid.
 */
public class ScenarioIS6 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord cash;
	private String existingChargeId;

	@Given("[I.S.6] a paid invoice and a cash payment mean")
	public void givenPaidInvoiceAndCash() {
		TClientsRecord client = DbFixtures.aClient();
		cash = DbFixtures.aCashForClient(client.id);
		invoice = DbFixtures.aPaidInvoiceForPaymentMean(cash);
		existingChargeId = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id).get(0).id;
	}

	@When("[I.S.6] I try to settle the invoice again with that payment mean")
	public void whenITryToSettleAgain() {
		errors.tryAndKeep(
				() -> service.settleInvoice(
						invoice.id, Map.of(cash.id, new BigDecimal("100.00"))
					)
			);
	}

	@Then("[I.S.6] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.6] the existing charges and accumulated amounts remain unchanged")
	public void thenExistingChargeAndBalanceRemain() {
		TInvoicesRecord loadedInvoice = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loadedInvoice);
		assertEquals("PAID", loadedInvoice.state);

		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(1, charges.size());
		
		TChargesRecord remainingCharge = charges.get(0);
		assertEquals(existingChargeId, remainingCharge.id);
		assertEquals(invoice.id, remainingCharge.invoice_Id);
		assertEquals(cash.id, remainingCharge.paymentMean_Id);
		assertEquals(0, new BigDecimal("100.00").compareTo(remainingCharge.charge));

		TPaymentMeansRecord loadedCash = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loadedCash);
		assertEquals(0, new BigDecimal("100.00").compareTo(loadedCash.accumulated));
	}
}
