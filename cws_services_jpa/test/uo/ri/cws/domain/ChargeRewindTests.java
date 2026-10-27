package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;

/**
 * Scenarios:
 * - Rewinding compensates the payment mean and unlinks the charge
 * - A charge cannot be rewound twice
 * - A failed compensation leaves the charge linked
 */
class ChargeRewindTests {

	private static final Money AMOUNT = Money.of("100.00");

	private Invoice invoice;
	private PaymentMean paymentMean;
	private Charge charge;

	@BeforeEach
	void setUp() {
		invoice = new Invoice(1L);
		paymentMean = mock(PaymentMean.class);
		charge = new Charge(invoice, paymentMean, AMOUNT);
		clearInvocations(paymentMean);
	}

	/**
	 * GIVEN: a charge linked to an invoice and a payment mean
	 * WHEN: the charge is rewound
	 * THEN: the payment mean is compensated
	 * AND: both association ends are unlinked
	 */
	@Test
	void testRewind() {
		charge.rewind();

		verify(paymentMean).rewind(AMOUNT);
		assertTrue(invoice.getCharges().isEmpty());
		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
	}

	/**
	 * GIVEN: a charge that has already been rewound
	 * WHEN: trying to rewind it again
	 * THEN: an IllegalStateException is thrown without another compensation
	 */
	@Test
	void testCannotBeRewoundTwice() {
		charge.rewind();
		clearInvocations(paymentMean);

		assertThrows(IllegalStateException.class, charge::rewind);

		verifyNoInteractions(paymentMean);
	}

	/**
	 * GIVEN: a payment mean that cannot compensate a charge
	 * WHEN: the charge is rewound
	 * THEN: the failure is propagated
	 * AND: the charge remains linked at both ends
	 */
	@Test
	void testFailedCompensationKeepsAssociations() {
		doThrow(new IllegalStateException()).when(paymentMean).rewind(AMOUNT);

		assertThrows(IllegalStateException.class, charge::rewind);

		verify(paymentMean).rewind(AMOUNT);
		assertSame(invoice, charge.getInvoice());
		assertSame(paymentMean, charge.getPaymentMean());
		assertTrue(invoice.getCharges().contains(charge));
	}
}
