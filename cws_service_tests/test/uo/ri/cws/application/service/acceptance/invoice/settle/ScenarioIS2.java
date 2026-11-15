package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TChargesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.S.2] Settle an invoice with a card valid through the current month.
 */
public class ScenarioIS2 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord card;

	@Given("[I.S.2] an issued invoice for 121.00 and a card valid through the current month")
	public void givenIssuedInvoiceAndValidCard() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		card = DbFixtures.aCreditCardForClientValidThru(client.id, YearMonth.now());
	}

	@When("[I.S.2] I settle the invoice by charging 121.00 to the card")
	public void whenISettleWithCard() throws BusinessException {
		service.settleInvoice(invoice.id, Map.of(card.id, new BigDecimal("121.00")));
	}

	@Then("[I.S.2] the invoice is marked as PAID")
	public void thenInvoiceIsPaid() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("PAID", loaded.state);
	}

	@Then("[I.S.2] one charge for 121.00 links the invoice and the card")
	public void thenOneCardChargeExists() {
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(1, charges.size());
		
		TChargesRecord charge = charges.get(0);
		assertEquals(invoice.id, charge.invoice_Id);
		assertEquals(card.id, charge.paymentMean_Id);
		assertEquals(0, new BigDecimal("121.00").compareTo(charge.charge));
	}

	@Then("[I.S.2] the card accumulated amount increases by 121.00")
	public void thenCardAccumulatedIncreases() {
		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, card.id);
		assertNotNull(loaded);
		assertEquals(0, new BigDecimal("121.00").compareTo(loaded.accumulated));
	}
}
