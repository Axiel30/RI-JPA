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
 * Scenario: [I.S.12] Compare and record charge amounts at cent precision.
 */
public class ScenarioIS12 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TInvoicesRecord invoice;
	private TPaymentMeansRecord firstCash;
	private TPaymentMeansRecord secondCash;

	@Given("[I.S.12] an issued invoice for 121.00 and two cash payment means")
	public void givenIssuedInvoiceAndTwoCashPaymentMeans() {
		TClientsRecord client = DbFixtures.aClient();
		invoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		assertEquals(0, new BigDecimal("121.00").compareTo(invoice.total));
		
		firstCash = DbFixtures.aCashForClient(client.id);
		TClientsRecord secondClient = DbFixtures.aClient();
		secondCash = DbFixtures.aCashForClient(secondClient.id);
	}

	@When("[I.S.12] I settle it by charging 60.005 to one and 60.995 to the other")
	public void whenISettleAtCentPrecision() throws BusinessException {
		service.settleInvoice(
				invoice.id, 
				Map.of(
						firstCash.id, new BigDecimal("60.005"),
						secondCash.id, new BigDecimal("60.995")
					)
			);
	}

	@Then("[I.S.12] the invoice is marked as PAID")
	public void thenInvoiceIsPaid() {
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		assertNotNull(loaded);
		assertEquals("PAID", loaded.state);
	}

	@Then("[I.S.12] the charges are recorded as 60.00 and 61.00 respectively")
	public void thenChargesAreRoundedToCents() {
		List<TChargesRecord> charges = Db.findAllBy(TChargesRecord.class, "invoice_Id", invoice.id);
		assertEquals(2, charges.size());
		
		TChargesRecord firstCharge = charges.stream()
				.filter(charge -> firstCash.id.equals(charge.paymentMean_Id))
				.findFirst()
				.orElseThrow();
		assertEquals(invoice.id, firstCharge.invoice_Id);
		assertEquals(0, new BigDecimal("60.00").compareTo(firstCharge.charge));
		
		TChargesRecord secondCharge = charges.stream()
				.filter(charge -> secondCash.id.equals(charge.paymentMean_Id))
				.findFirst()
				.orElseThrow();
		assertEquals(invoice.id, secondCharge.invoice_Id);
		assertEquals(0, new BigDecimal("61.00").compareTo(secondCharge.charge));
	}

	@Then("[I.S.12] the payment means accumulated amounts increase by those recorded amounts")
	public void thenAccumulatedAmountsIncreaseByRoundedCharges() {
		TPaymentMeansRecord loadedFirst = Db.findById(TPaymentMeansRecord.class, firstCash.id);
		assertNotNull(loadedFirst);
		assertEquals(0, new BigDecimal("60.00").compareTo(loadedFirst.accumulated));
		
		TPaymentMeansRecord loadedSecond = Db.findById(TPaymentMeansRecord.class, secondCash.id);
		assertNotNull(loadedSecond);
		assertEquals(0, new BigDecimal("61.00").compareTo(loadedSecond.accumulated));
	}
}
