package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;

/**
 * Integration tests for the complete Invoice state machine.
 *
 * Scenarios:
 * - An ISSUED invoice cannot be unsettled
 * - A PAID invoice cannot add work orders
 * - A PAID invoice cannot remove work orders
 * - A PAID invoice cannot be settled again
 */
class InvoiceLifecycleTests {

	private static final Money WORK_ORDER_AMOUNT = Money.of("100.00");
	private static final Money INVOICE_TOTAL = Money.of("121.00");

	private WorkOrder workOrder;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(WORK_ORDER_AMOUNT);
		invoice = new Invoice(1L);
		invoice.addWorkOrder(workOrder);
	}

	/**
	 * GIVEN: an invoice in ISSUED state
	 * WHEN: trying to unsettle it
	 * THEN: an IllegalStateException is thrown and it remains unchanged
	 */
	@Test
	void testIssuedInvoiceCannotBeUnsettled() {
		assertThrows(IllegalStateException.class, () -> invoice.unSettle());

		assertFalse(invoice.isSettled());
		assertTrue(invoice.getCharges().isEmpty());
	}

	/**
	 * GIVEN: an invoice in PAID state
	 * WHEN: trying to add another work order
	 * THEN: an IllegalStateException is thrown and it remains unchanged
	 */
	@Test
	void testPaidInvoiceCannotAddWorkOrders() {
		settleInvoiceWithOnePaymentMean( invoice );
		WorkOrder another = DomainTestMocksFactory.oneApprovedWorkOrder(WORK_ORDER_AMOUNT);

		assertThrows(
				IllegalStateException.class,
				() -> invoice.addWorkOrder(another)
			);

		assertEquals(1, invoice.getWorkOrders().size());
		assertFalse(invoice.getWorkOrders().contains(another));
	}

	/**
	 * GIVEN: an invoice in PAID state
	 * WHEN: trying to remove one of its work orders
	 * THEN: an IllegalStateException is thrown and it remains unchanged
	 */
	@Test
	void testPaidInvoiceCannotRemoveWorkOrders() {
		settleInvoiceWithOnePaymentMean( invoice );

		assertThrows(
				IllegalStateException.class,
				() -> invoice.removeWorkOrder(workOrder)
			);

		assertEquals(1, invoice.getWorkOrders().size());
		assertTrue(invoice.getWorkOrders().contains(workOrder));
	}

	/**
	 * GIVEN: an invoice in PAID state
	 * WHEN: trying to settle it again
	 * THEN: an IllegalStateException is thrown and it remains unchanged
	 */
	@Test
	void testPaidInvoiceCannotBeSettledAgain() {
		PaymentMean paymentMean = settleInvoiceWithOnePaymentMean( invoice );

		assertThrows(
				IllegalStateException.class,
				() -> invoice.settle(Map.of(paymentMean, INVOICE_TOTAL))
			);

		assertTrue(invoice.isSettled());
		assertEquals(1, invoice.getCharges().size());
	}

	private static PaymentMean settleInvoiceWithOnePaymentMean(Invoice invoice) {
		PaymentMean paymentMean = mock(PaymentMean.class);
		when(paymentMean.canBeCharged(INVOICE_TOTAL)).thenReturn(true);
		invoice.settle(Map.of(paymentMean, INVOICE_TOTAL));
		return paymentMean;
	}
}
