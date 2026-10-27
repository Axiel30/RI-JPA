package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.InvoiceBuilder;

/**
 * Scenarios:
 * - A paid invoice over 500 euro that has not been used can generate a voucher
 * - A paid invoice of exactly 500 euro cannot generate a voucher
 * - A paid invoice below 500 euro cannot generate a voucher
 * - An unpaid invoice over 500 euro cannot generate a voucher
 * - A paid invoice over 500 euro already used for a voucher cannot generate another one
 */
class InvoiceCanGenerateVoucher30Tests {
	/**
	 * GIVEN: a paid invoice with a total over 500 euro
	 * AND: the invoice has not been used for a voucher
	 * WHEN: checking whether it can generate a 30 euro voucher
	 * THEN: it can generate the voucher
	 */
	@Test
	void testPaidInvoiceOver500CanGenerateVoucher() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("500.00")
			.paid()
			.build();

		boolean result = invoice.canGenerateVoucher30();

		assertTrue(result);
	}

	/**
	 * GIVEN: a paid invoice with a total of exactly 500 euro
	 * WHEN: checking whether it can generate a 30 euro voucher
	 * THEN: it cannot generate the voucher
	 */
	@Test
	void testPaidInvoiceOfExactly500CannotGenerateVoucher() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("413.22")
			.paid()
			.build();
		assertEquals(Money.of("500.00"), invoice.getTotal());

		boolean result = invoice.canGenerateVoucher30();

		assertFalse(result);
	}

	/**
	 * GIVEN: a paid invoice with a total below 500 euro
	 * WHEN: checking whether it can generate a 30 euro voucher
	 * THEN: it cannot generate the voucher
	 */
	@Test
	void testPaidInvoiceBelow500CannotGenerateVoucher() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("100.00")
			.paid()
			.build();

		boolean result = invoice.canGenerateVoucher30();

		assertFalse(result);
	}

	/**
	 * GIVEN: an unpaid invoice with a total over 500 euro
	 * WHEN: checking whether it can generate a 30 euro voucher
	 * THEN: it cannot generate the voucher
	 */
	@Test
	void testUnpaidInvoiceOver500CannotGenerateVoucher() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("500.00")
			.build();

		boolean result = invoice.canGenerateVoucher30();

		assertFalse(result);
	}

	/**
	 * GIVEN: a paid invoice over 500 euro already used for a voucher
	 * WHEN: checking whether it can generate another 30 euro voucher
	 * THEN: it cannot generate another voucher
	 */
	@Test
	void testUsedInvoiceCannotGenerateAnotherVoucher() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("500.00")
			.paid()
			.build();
		invoice.markAsUsedForVoucher();

		boolean result = invoice.canGenerateVoucher30();

		assertFalse(result);
	}
}
