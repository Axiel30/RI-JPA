package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.YearMonth;
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
 * Scenario: [I.S.9] Reject an expired card.
 */
public class ScenarioIS9 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord card;

	@Given("[I.S.9] an issued invoice for 121.00 and a card expired before the current month")
	public void givenIssuedInvoiceAndExpiredCard() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		
		YearMonth currentMonth = YearMonth.now();
		card = DbFixtures.aCreditCardForClientValidThru(client.id, currentMonth.minusMonths(1));
	}

	@When("[I.S.9] I try to settle the invoice by charging 121.00 to the card")
	public void whenITryToUseExpiredCard() {
		errors.tryAndKeep(
					() -> service.settleInvoice(
							invoice.id, Map.of(card.id, new BigDecimal("121.00"))
					)
				);
	}

	@Then("[I.S.9] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.9] the invoice remains ISSUED with no charges")
	public void thenInvoiceIsUnchanged() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("ISSUED", loaded.state);
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertTrue(charges.isEmpty());
	}

	@Then("[I.S.9] the card accumulated amount remains unchanged")
	public void thenCardIsUnchanged() {
		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, card.id);
		assertNotNull(loaded);
		assertEquals(0, BigDecimal.ZERO.compareTo(loaded.accumulated));
	}
}
