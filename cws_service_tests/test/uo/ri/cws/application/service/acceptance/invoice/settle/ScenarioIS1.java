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
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TChargesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.S.1] Settle an issued invoice with cash.
 */
public class ScenarioIS1 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord cash;

	@Given("[I.S.1] an issued invoice for 121.00 and a cash payment mean")
	public void givenIssuedInvoiceAndCash() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[I.S.1] I settle the invoice by charging 121.00 to the cash payment mean")
	public void whenISettleWithCash() throws BusinessException {
		service.settleInvoice(invoice.id, Map.of(cash.id, new BigDecimal("121.00")));
	}

	@Then("[I.S.1] the invoice is marked as PAID")
	public void thenInvoiceIsPaid() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("PAID", loaded.state);
	}

	@Then("[I.S.1] one charge for 121.00 links the invoice and the cash payment mean")
	public void thenOneCashChargeExists() {
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(1, charges.size());
		
		TChargesRecord charge = charges.get(0);
		assertEquals(invoice.id, charge.invoice_Id);
		assertEquals(cash.id, charge.paymentMean_Id);
		assertEquals(0, new BigDecimal("121.00").compareTo(charge.charge));
	}

	@Then("[I.S.1] the cash payment mean accumulated amount increases by 121.00")
	public void thenCashAccumulatedIncreases() {
		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loaded);
		assertEquals(0, new BigDecimal("121.00").compareTo(loaded.accumulated));
	}
}
