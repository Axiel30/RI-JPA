package uo.ri.cws.application.service.acceptance.invoice.findbynumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.FbN.1] Find an invoice among several invoices.
 */
public class ScenarioIFbN1 {
	private final InvoicingService service = Factories.service.forCreateInvoiceService();
	private TInvoicesRecord requestedInvoice;
	private Optional<InvoiceDto> result;

	@Given("[I.FbN.1] two invoices with different numbers")
	public void givenTwoInvoicesWithDifferentNumbers() {
		TClientsRecord client = DbFixtures.aClient();
		requestedInvoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("100.00"));
		TInvoicesRecord otherInvoice = DbFixtures.anInvoiceIssuedForClientOf(client.id, new BigDecimal("200.00"));
		assertNotEquals(requestedInvoice.number, otherInvoice.number);
	}

	@When("[I.FbN.1] I search for one invoice by its number")
	public void whenISearchByNumber() throws BusinessException {
		result = service.findInvoiceByNumber(requestedInvoice.number);
	}

	@Then("[I.FbN.1] I get the invoice with the requested id, number, date and version")
	public void thenIdentityAndDateMatch() {
		assertTrue(result.isPresent());
		
		InvoiceDto dto = result.get();
		assertEquals(requestedInvoice.id, dto.id);
		assertEquals(requestedInvoice.number.longValue(), dto.number);
		assertEquals(requestedInvoice.date.toLocalDate(), dto.date);
		assertEquals(requestedInvoice.version, dto.version);
	}

	@Then("[I.FbN.1] its subtotal, VAT percentage, VAT amount and total match the invoice")
	public void thenAmountsMatch() {
		assertTrue(result.isPresent());
		
		InvoiceDto dto = result.get();
		assertEquals(0, requestedInvoice.subtotal.compareTo(dto.subtotal));
		assertEquals(0, requestedInvoice.vatrate.compareTo(dto.vatPercentage));
		assertEquals(0, requestedInvoice.vatamount.compareTo(dto.vatAmount));
		assertEquals(0, requestedInvoice.total.compareTo(dto.total));
	}
}
