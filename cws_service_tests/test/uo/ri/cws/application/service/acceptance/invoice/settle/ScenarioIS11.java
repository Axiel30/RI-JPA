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
 * Scenario: [I.S.11] Reject all charges if one payment mean cannot be charged.
 */
public class ScenarioIS11 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TClientsRecord client;
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord cash;
	private TPaymentMeansRecord card;

	@Given("[I.S.11] an issued invoice for 121.00")
	public void givenIssuedInvoiceFor121() {
		client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
	}

	@Given("[I.S.11] a cash payment mean and a card expired before the current month")
	public void givenCashAndExpiredCard() {
		cash = DbFixtures.aCashForClient(client.id);
		YearMonth currentMonth = YearMonth.now();
		card = DbFixtures.aCreditCardForClientValidThru(client.id, currentMonth.minusMonths(1));
	}

	@When("[I.S.11] I try to settle it with 71.00 in cash and 50.00 on the card")
	public void whenITryToSplitWithExpiredCard() {
		errors.tryAndKeep(	
				() -> service.settleInvoice(
						invoice.id, 
						Map.of(
							cash.id, new BigDecimal("71.00"),
							card.id, new BigDecimal("50.00")
						)
				));
	}

	@Then("[I.S.11] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.11] the invoice remains ISSUED with no charges")
	public void thenInvoiceIsUnchanged() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("ISSUED", loaded.state);
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertTrue(charges.isEmpty());
	}

	@Then("[I.S.11] neither payment mean accumulated amount changes")
	public void thenNeitherPaymentMeanChanges() {
		TPaymentMeansRecord loadedCash = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loadedCash);
		assertEquals(0, BigDecimal.ZERO.compareTo(loadedCash.accumulated));
		
		TPaymentMeansRecord loadedCard = Db.findById(TPaymentMeansRecord.class, card.id);
		assertNotNull(loadedCard);
		assertEquals(0, BigDecimal.ZERO.compareTo(loadedCard.accumulated));
	}
}
