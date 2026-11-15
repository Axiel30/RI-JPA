package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Scenario: [I.S.8] Reject charges whose sum differs from the invoice total.
 */
public class ScenarioIS8 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord cash;

	@Given("[I.S.8] an issued invoice for 121.00 and a cash payment mean")
	public void givenIssuedInvoiceAndCash() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[I.S.8] I try to settle the invoice by charging 120.99")
	public void whenIChargeWrongTotal() {
		errors.tryAndKeep(
				() -> service.settleInvoice(
						invoice.id, Map.of(cash.id, new BigDecimal("120.99"))
					)
			);
	}

	@Then("[I.S.8] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.8] the invoice remains ISSUED with no charges")
	public void thenInvoiceIsUnchanged() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("ISSUED", loaded.state);
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertTrue(charges.isEmpty());
	}

	@Then("[I.S.8] the cash payment mean accumulated amount remains unchanged")
	public void thenCashIsUnchanged() {
		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loaded);
		assertEquals(0, BigDecimal.ZERO.compareTo(loaded.accumulated));
	}
}
