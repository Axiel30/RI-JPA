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
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenario: [I.S.10] Reject a voucher without enough available balance.
 */
public class ScenarioIS10 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord voucher;

	@Given("[I.S.10] an issued invoice for 121.00 and a voucher with 120.00 available")
	public void givenIssuedInvoiceAndVoucher() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		voucher = DbFixtures.aVoucherForClientOf(
				client.id, new BigDecimal("120.00"), new BigDecimal("120.00"), "Settlement voucher");
	}

	@When("[I.S.10] I try to settle the invoice by charging 121.00 to the voucher")
	public void whenITryToOverchargeVoucher() {
		errors.tryAndKeep(
				() -> service.settleInvoice(
							invoice.id, Map.of(voucher.id, new BigDecimal("121.00"))
					)
				);
	}

	@Then("[I.S.10] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.10] the invoice remains ISSUED with no charges")
	public void thenInvoiceIsUnchanged() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("ISSUED", loaded.state);
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertTrue(charges.isEmpty());
	}

	@Then("[I.S.10] the voucher available and accumulated amounts remain unchanged")
	public void thenVoucherIsUnchanged() {
		TVouchersRecord loadedVoucher = Db.findById(TVouchersRecord.class, voucher.id);
		assertNotNull(loadedVoucher);
		assertEquals(0, new BigDecimal("120.00").compareTo(loadedVoucher.available));
		
		TPaymentMeansRecord loadedPaymentMean = Db.findById(TPaymentMeansRecord.class, voucher.id);
		assertNotNull(loadedPaymentMean);
		assertEquals(0, BigDecimal.ZERO.compareTo(loadedPaymentMean.accumulated));
	}
}
