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
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenario: [I.S.5] Reject settlement of a non-existing invoice.
 */
public class ScenarioIS5 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private final ExceptionBox errors = new ExceptionBox();
	private TPaymentMeansRecord cash;

	@Given("[I.S.5] a cash payment mean")
	public void givenCash() {
		cash = DbFixtures.aCashForClient(DbFixtures.aClient().id);
	}

	@When("[I.S.5] I try to settle a non-existing invoice with that payment mean")
	public void whenITryToSettleMissingInvoice() {
		String missingInvoiceId = UUID.randomUUID().toString();
		errors.tryAndKeep(
				() -> service.settleInvoice(
						missingInvoiceId, 
						Map.of(cash.id, new BigDecimal("121.00"))
					)
			);
	}

	@Then("[I.S.5] a business error happens with an explaining message")
	public void thenBusinessError() {
		errors.assertBusinessExceptionWithMessage();
	}

	@Then("[I.S.5] no charge is created or accumulated amount changed")
	public void thenNoChargeOrAccumulatedChange() {
		assertTrue(Db.findAll(TChargesRecord.class).isEmpty());
		
		TPaymentMeansRecord loaded = Db.findById(TPaymentMeansRecord.class, cash.id);
		assertNotNull(loaded);
		assertEquals(0, BigDecimal.ZERO.compareTo(loaded.accumulated));
	}
}
