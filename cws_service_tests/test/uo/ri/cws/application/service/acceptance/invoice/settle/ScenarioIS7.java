package uo.ri.cws.application.service.acceptance.invoice.settle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

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
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenario: [I.S.7] Reject a non-existing payment mean.
 */
public class ScenarioIS7 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TInvoicesRecord invoice;

	@Given("[I.S.7] an issued invoice for 121.00")
	public void givenIssuedInvoiceFor121() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
	}

	@When("[I.S.7] I try to settle it by charging 121.00 to a non-existing payment mean")
	public void whenITryToUseMissingPaymentMean() {
		String missingPaymentMeanId = UUID.randomUUID().toString();
		errors.tryAndKeep(
				() -> service.settleInvoice(
						invoice.id, Map.of(missingPaymentMeanId, new BigDecimal("121.00"))
					)
			);
	}

	@Then("[I.S.7] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.7] the invoice remains ISSUED with no charges")
	public void thenInvoiceIsUnchanged() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("ISSUED", loaded.state);
		assertTrue(Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id).isEmpty());
	}
}
