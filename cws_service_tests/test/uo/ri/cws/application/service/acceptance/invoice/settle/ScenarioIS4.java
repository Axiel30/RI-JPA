package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
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
 * Scenario: [I.S.4] Settle an invoice with a payment mean owned by another client.
 */
public class ScenarioIS4 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord otherClientCash;

	@Given("[I.S.4] an issued invoice for 121.00 and a cash payment mean owned by another client")
	public void givenIssuedInvoiceAndOtherClientsCash() {
		TClientsRecord invoiceClient = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(invoiceClient.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		
		TClientsRecord otherClient = DbFixtures.aClient();
		assertNotEquals(invoiceClient.id, otherClient.id);
		otherClientCash = DbFixtures.aCashForClient(otherClient.id);
	}

	@When("[I.S.4] I settle the invoice by charging 121.00 to that payment mean")
	public void whenISettleWithOtherClientsCash() throws BusinessException {
		service.settleInvoice(invoice.id, Map.of(otherClientCash.id, new BigDecimal("121.00")));
	}

	@Then("[I.S.4] the invoice is marked as PAID")
	public void thenInvoiceIsPaid() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("PAID", loaded.state);
	}

	@Then("[I.S.4] the charge is linked to that payment mean")
	public void thenChargeUsesOtherClientsCash() {
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(1, charges.size());
		
		TChargesRecord charge = charges.get(0);
		assertEquals(invoice.id, charge.invoice_Id);
		assertEquals(otherClientCash.id, charge.paymentMean_Id);
		assertEquals(0, new BigDecimal("121.00").compareTo(charge.charge));

		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, otherClientCash.id);
		assertNotNull(loaded);
		assertEquals(0, new BigDecimal("121.00").compareTo(loaded.accumulated));
	}
}
