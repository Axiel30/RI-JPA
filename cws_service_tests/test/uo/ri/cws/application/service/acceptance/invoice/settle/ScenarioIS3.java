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
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TVouchersRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.S.3] Split an invoice between a voucher and cash.
 */
public class ScenarioIS3 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TClientsRecord client;
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord voucher;
	private TPaymentMeansRecord cash;

	@Given("[I.S.3] an issued invoice for 121.00")
	public void givenIssuedInvoiceFor121() {
		client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
	}

	@Given("[I.S.3] a voucher with 50.00 available and a cash payment mean")
	public void givenVoucherAndCash() {
		voucher = DbFixtures.aVoucherForClientOf(
				client.id, 
				new BigDecimal("50.00"), 
				new BigDecimal("50.00"), 
				"Settlement voucher"
			);
		cash = DbFixtures.aCashForClient(client.id);
	}

	@When("[I.S.3] I settle the invoice with 50.00 from the voucher and 71.00 in cash")
	public void whenISettleWithVoucherAndCash() throws BusinessException {
		service.settleInvoice(
				invoice.id, 
				Map.of(
					voucher.id, new BigDecimal("50.00"),
					cash.id, new BigDecimal("71.00")
				)
			);
	}

	@Then("[I.S.3] the invoice is marked as PAID with two charges for the requested amounts")
	public void thenInvoiceIsPaidWithTwoCharges() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("PAID", loaded.state);

		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(2, charges.size());
		
		TChargesRecord voucherCharge = charges.stream()
				.filter(charge -> voucher.id.equals(charge.paymentMean_Id))
				.findFirst()
				.orElseThrow();
		assertEquals(invoice.id, voucherCharge.invoice_Id);
		assertEquals(0, new BigDecimal("50.00").compareTo(voucherCharge.charge));
		
		TChargesRecord cashCharge = charges.stream()
				.filter(charge -> cash.id.equals(charge.paymentMean_Id))
				.findFirst()
				.orElseThrow();
		assertEquals(invoice.id, cashCharge.invoice_Id);
		assertEquals(0, new BigDecimal("71.00").compareTo(cashCharge.charge));
	}

	@Then("[I.S.3] the voucher available amount becomes 0.00")
	public void thenVoucherIsDepleted() {
		TVouchersRecord loaded = Db.findById(TVouchersRecord.class, voucher.id);
		assertNotNull(loaded);
		assertEquals(0, BigDecimal.ZERO.compareTo(loaded.available));
	}

	@Then("[I.S.3] each payment mean accumulated amount increases by its charge")
	public void thenAccumulatedAmountsIncrease() {
		TPaymentMeansRecord loadedVoucher = Db.findById(TPaymentMeansRecord.class, voucher.id);
		assertNotNull(loadedVoucher);
		assertEquals(0, new BigDecimal("50.00").compareTo(loadedVoucher.accumulated));
		
		TPaymentMeansRecord loadedCash = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loadedCash);
		assertEquals(0, new BigDecimal("71.00").compareTo(loadedCash.accumulated));
	}
}
