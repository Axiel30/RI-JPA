package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo.ri.util.InvoiceBuilder;

/**
 * Scenarios:
 * - Marking an unused invoice for a voucher changes it to used
 */
class InvoiceMarkAsUsedForVoucherTests {
	/**
	 * GIVEN: an unused paid invoice over 500 euro that can generate a voucher
	 * WHEN: marking the invoice as used for a voucher
	 * THEN: it can no longer generate a voucher
	 */
	@Test
	void testMarkInvoiceAsUsed() {
		Invoice invoice = new InvoiceBuilder()
			.withSubtotal("500.00")
			.paid()
			.build();
		assertTrue(invoice.canGenerateVoucher30());

		invoice.markAsUsedForVoucher();

		assertFalse(invoice.canGenerateVoucher30());
	}
}
