package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - Unsettling a paid invoice removes and unlinks every charge
 * - Unsettling restores accumulated amounts on every payment mean
 * - Unsettling a voucher also restores its available amount
 * - Work orders remain invoiced and linked after unsettling
 * - An unsettled invoice can be settled again with different payment means
 */
class InvoiceUnSettleTests {

	private static final Money WORK_ORDER_AMOUNT = Money.of("100.00");
	private static final Money INVOICE_TOTAL = Money.of("121.00");

	private WorkOrder workOrder;
	private Invoice invoice;
	private PaymentMean cash;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(WORK_ORDER_AMOUNT);

		invoice = new Invoice(1L, List.of(workOrder));
		cash = new Cash(new Client(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			));
	}

	/**
	 * GIVEN: a paid invoice with a cash charge
	 * WHEN: the invoice is unsettled
	 * THEN: it returns to ISSUED
	 * AND: all charges are removed and unlinked
	 * AND: the cash accumulated is restored
	 */
	@Test
	void testUnSettleCashCharge() {
		invoice.settle(Map.of(cash, INVOICE_TOTAL));
		Charge charge = invoice.getCharges().iterator().next();

		invoice.unSettle();

		assertFalse(invoice.isSettled());
		assertTrue(invoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, cash.getAccumulated());
		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
	}

	/**
	 * GIVEN: a paid invoice split between cash and a voucher
	 * WHEN: the invoice is unsettled
	 * THEN: both accumulated amounts are restored
	 * AND: the voucher available amount is restored
	 */
	@Test
	void testUnSettleSeveralChargesRestoresVoucher() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);
		invoice.settle(Map.of(
				cash, Money.of("71.00"),
				voucher, Money.of("50.00")
			));

		invoice.unSettle();

		assertFalse(invoice.isSettled());
		assertTrue(invoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, cash.getAccumulated());
		assertEquals(Money.ZERO, voucher.getAccumulated());
		assertEquals(Money.of("100.00"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: a paid invoice with a linked work order
	 * WHEN: the invoice is unsettled
	 * THEN: the work order remains linked and invoiced
	 */
	@Test
	void testWorkOrdersAreNotAffected() {
		invoice.settle(Map.of(cash, INVOICE_TOTAL));

		invoice.unSettle();

		assertTrue(invoice.getWorkOrders().contains(workOrder));
		assertSame(workOrder, invoice.getWorkOrders().iterator().next());
		verify(workOrder, never()).uninvoice();
		verify(workOrder, never()).unapprove();
	}

	/**
	 * GIVEN: a paid invoice that is later unsettled
	 * WHEN: settling it again with a different payment mean
	 * THEN: the second settlement succeeds independently
	 */
	@Test
	void testInvoiceCanBeSettledAgain() {
		invoice.settle(Map.of(cash, INVOICE_TOTAL));
		invoice.unSettle();
		Voucher voucher = new Voucher(
				new VoucherCode("V-2"),
				Money.of("200.00")
			);

		invoice.settle(Map.of(voucher, INVOICE_TOTAL));

		assertTrue(invoice.isSettled());
		assertEquals(1, invoice.getCharges().size());
		assertEquals(Money.ZERO, cash.getAccumulated());
		assertEquals(INVOICE_TOTAL, voucher.getAccumulated());
		assertEquals(Money.of("79.00"), voucher.getAvailableAmount());
	}
}
