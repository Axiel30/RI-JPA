package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;

/**
 * Scenarios: 
 * - Removing a work order from an invoice unlinks, recomputes the amount and 
 * 		puts the work order back to APPROVED
 * - Removing a work order from an invoice with multiple work orders
 * - Removing a work order from an invoice that does not have throws IAE
 * - Removing a work order from a null invoice throws IAE
 */
class InvoiceRemoveWorkOrderTests {

	private static final Money AMOUNT = Money.of("100.0");
	private static final Money ZERO = Money.of("0.0");
	private static final Percentage VAT = Percentage.ofPercent(21);
	
	private WorkOrder workOrder;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(AMOUNT);

		invoice = new Invoice(1000L);
		invoice.addWorkOrder(workOrder);
	}

	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing the work order from the invoice
	 * THEN: the work order is removed from the invoice
	 * AND: the invoice amount is recomputed
	 * AND: the work order is unlinked from the invoice
	 * AND: the work order state is APPROVED
	 */
	@Test
	void testUnlinkOnInvoice() {
	    invoice.removeWorkOrder(workOrder);

	    // expected values after removing the only work order
	    Money expectedNet = ZERO;
	    Money expectedVat = ZERO;
	    Money expectedGross = ZERO;

	    // ASSERTS
	    assertTrue(invoice.getWorkOrders().isEmpty());

	    assertEquals(expectedNet, invoice.getSubtotal());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedGross, invoice.getTotal());

	 // vatRate stays as the one that corresponds to the invoice date
	    assertEquals(VAT, invoice.getVatRate());

	    verify(workOrder).uninvoice();
	}

	/**
	 * GIVEN: an invoice with two work orders
	 * WHEN: removing one of them from the invoice
	 * THEN: the invoice amount is recomputed
	 * AND: the work order is unlinked from the invoice
	 * AND: the work order state goes back to APPROVED
	 */
	@Test
	void testUnlinkOneOfTwoOnInvoice() {
	    WorkOrder wo2 = DomainTestMocksFactory.oneApprovedWorkOrder(AMOUNT);

	    invoice.addWorkOrder(wo2);

	    // expected net = AMOUNT
	    Money expectedNet = AMOUNT;

	    // expected vat = AMOUNT * VAT_RATE
	    Money expectedVat =
	            Money.calc(AMOUNT)
	                 .times(VAT.asRatio())
	                 .toMoneyRoundedToCents();

	    // expected gross = net + vat
	    Money expectedGross =
	            Money.calc(expectedNet)
	                 .add(expectedVat)
	                 .toMoneyRoundedToCents();

	    invoice.removeWorkOrder(wo2);

	    // ASSERTS
	    assertEquals(expectedNet, invoice.getSubtotal());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedGross, invoice.getTotal());
	    assertEquals(VAT, invoice.getVatRate()); 

	    assertFalse(invoice.getWorkOrders().contains(wo2));
	    assertEquals(1, invoice.getWorkOrders().size());

	    verify(wo2).uninvoice();
	    verify(workOrder, never()).uninvoice();
	}

	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing a work order that is not in the invoice
	 * THEN: an IAE is thrown 
	 * AND: the invoice amount is not changed
	 */
	@Test
	void testUnlinkWorkOrderNotInInvoice() {
	    Money oldNet = invoice.getSubtotal();
	    Money oldVat = invoice.getVatAmount();
	    Money oldGross = invoice.getTotal();
	    Percentage oldRate = invoice.getVatRate();

	    WorkOrder wo2 = mock(WorkOrder.class);

	    assertThrows(
	            IllegalArgumentException.class,
	            () -> invoice.removeWorkOrder(wo2)
	    );

	    // amounts unchanged
	    assertEquals(oldNet, invoice.getSubtotal());
	    assertEquals(oldVat, invoice.getVatAmount());
	    assertEquals(oldGross, invoice.getTotal());
	    assertEquals(oldRate, invoice.getVatRate());

	    assertTrue(invoice.getWorkOrders().contains(workOrder));
	    assertEquals(1, invoice.getWorkOrders().size());

	    verify(workOrder, never()).uninvoice();
	    verify(wo2, never()).uninvoice();
	}

	/**
	 * GIVEN: an invoice with a work order
	 * WHEN: removing a null work order from the invoice
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the invoice amount is not changed
	 */
	@Test
	void testUnlinkNullWorkOrderThrowsIAE() {
	    Money oldNet = invoice.getSubtotal();
	    Money oldVat = invoice.getVatAmount();
	    Money oldGross = invoice.getTotal();
	    Percentage oldRate = invoice.getVatRate();

	    assertThrows(IllegalArgumentException.class,
	            () -> invoice.removeWorkOrder(null)
	    );

	    // unchanged
	    assertEquals(oldNet, invoice.getSubtotal());
	    assertEquals(oldVat, invoice.getVatAmount());
	    assertEquals(oldGross, invoice.getTotal());
	    assertEquals(oldRate, invoice.getVatRate());
	}

}
