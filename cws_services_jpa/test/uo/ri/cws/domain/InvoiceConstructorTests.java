package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;

/**
 * Scenarios: 
 * - A new invoice computes the subtotal, VAT and total amounts from its work orders, is not
 * 		settled, and marks the work orders as invoiced
 * - Invoice amounts are rounded to cents using HALF_EVEN
 * - A null date or a null list of work orders throws an IAE
 * - Creating an invoice with a list of work orders where at least one is not APPROVED must be
 * 		atomic: an exception is thrown and NONE of the work orders is added
 * - Creating an invoice with a list of work orders where at least one is null must be atomic:
 * 		an IllegalArgumentException is thrown and NONE of the work orders is added
 */
class InvoiceConstructorTests {

	private static final Money WO_AMOUNT = Money.of("100.0");
	private static final Percentage VAT_RATE = Percentage.ofPercent(21);

	private WorkOrder workOrder;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(WO_AMOUNT);
	}

	/**
	 * GIVEN: a work order 
	 * WHEN: we create an invoice with them 
	 * THEN: the invoice computes the right amount 
	 * AND: the invoice is in ISSUED state 
	 * AND: the work orders are in INVOICED state 
	 * AND: the work orders are linked to the invoice 
	 * AND: the invoice is linked to the work orders 
	 * AND: the invoice date is today
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
	    Invoice invoice = new Invoice(1000L, List.of(workOrder));

	    // expected net
	    Money expectedSubTotal = WO_AMOUNT;

	    // expected vat
	    Money expectedVat =
	            Money.calc(WO_AMOUNT)
	                 .times(VAT_RATE.asRatio())
	                 .toMoneyRoundedToCents();

	    // expected gross
	    Money expectedTotal =
	            Money.calc(expectedSubTotal)
	                 .add(expectedVat)
	                 .toMoneyRoundedToCents();

	    // ASSERTS
	    assertEquals(expectedSubTotal, invoice.getSubtotal());
	    assertEquals(VAT_RATE, invoice.getVatRate());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedTotal, invoice.getTotal());

	    assertTrue(invoice.getWorkOrders().contains(workOrder));
	    assertTrue(! invoice.isSettled());
	    verify(workOrder, times(1)).invoice();
	}

	/**
	 * GIVEN: a work order with an amount that will produce a VAT amount with more than 2 decimal places
	 * WHEN: we create an invoice with it
	 * THEN: the invoice amounts are rounded to cents using half-even rounding
	 */
	@Test
	void testInvoiceAmountsAreRoundedToCentsUsingHalfEven() {
		when(workOrder.getAmount()).thenReturn(Money.of("0.03"));

		Invoice invoice = new Invoice(1000L, List.of(workOrder));

		assertEquals(Money.of("0.03").amount(), invoice.getSubtotal().amount());
		assertEquals(Money.of("0.01").amount(), invoice.getVatAmount().amount());
		assertEquals(Money.of("0.04").amount(), invoice.getTotal().amount());
	}

	/**
	 * WHEN: we create an invoice with null values 
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testInvoiceThrowsExceptionIfNull() {
		LocalDate now = LocalDate.now();
		List<WorkOrder> wos = List.of(workOrder);

		assertThrows(IllegalArgumentException.class, () -> new Invoice(1000L, null, wos));
		assertThrows(IllegalArgumentException.class, () -> new Invoice(1000L, now, null));
	}

	/**
	 * GIVEN: a list with several work orders where one of them is not APPROVED
	 * WHEN: creating an invoice with that list
	 * THEN: an exception is thrown
	 * AND: the operation is atomic, so NONE of the work orders is added, including
	 * 		the ones that were approved
	 */
	@Test
	void testConstructorIsAtomicIfNotAllWorkOrdersAreApproved() {
		WorkOrder notApprovedWorkOrder = DomainTestMocksFactory.oneWorkOrder();
		List<WorkOrder> wos = List.of(workOrder, notApprovedWorkOrder);

		assertThrows(IllegalStateException.class, () -> new Invoice(1000L, wos));

		verify(workOrder, times(0)).invoice();
		verify(notApprovedWorkOrder, times(0)).invoice();
	}

	/**
	 * GIVEN: a list with several work orders where one of them is null
	 * WHEN: creating an invoice with that list
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the operation is atomic, so NONE of the work orders is added, including
	 * 		the ones that were not null
	 */
	@Test
	void testConstructorIsAtomicIfAnyWorkOrderIsNull() {
		List<WorkOrder> wos = Arrays.asList(workOrder, null);

		assertThrows(IllegalArgumentException.class, () -> new Invoice(1000L, wos));

		verify(workOrder, times(0)).invoice();
	}

}