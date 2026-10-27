package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - Rewinding a payment mean reduces its accumulated amount
 * - Rewinding a voucher also restores its available amount
 * - Rewinding does not depend on whether the payment mean can currently be charged
 * - A payment mean cannot be rewound beyond its accumulated amount
 * - Null, zero and negative compensation amounts are rejected
 */
class PaymentMeanRewindTests {

	private PaymentMean cash;

	@BeforeEach
	void setUp() {
		Client client = new Client(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
		cash = new Cash(client);
	}

	/**
	 * GIVEN: a payment mean with an accumulated amount
	 * WHEN: part of that amount is rewound
	 * THEN: accumulated is reduced by that amount
	 */
	@Test
	void testRewindReducesAccumulated() {
		cash.charge(Money.of("100.00"));

		cash.rewind(Money.of("40.00"));

		assertEquals(Money.of("60.00"), cash.getAccumulated());
	}

	/**
	 * GIVEN: a charged voucher
	 * WHEN: its charge is rewound
	 * THEN: accumulated is reduced
	 * AND: available is increased by the same amount
	 */
	@Test
	void testVoucherRewindRestoresAvailable() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);
		voucher.charge(Money.of("40.00"));

		voucher.rewind(Money.of("40.00"));

		assertEquals(Money.ZERO, voucher.getAccumulated());
		assertEquals(Money.of("100.00"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: a charged payment mean that can no longer be charged
	 * WHEN: its charge is rewound
	 * THEN: the compensation still succeeds
	 */
	@Test
	void testRewindIgnoresCurrentChargeability() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();
		paymentMean.charge(Money.TEN);
		paymentMean.chargeable = false;

		paymentMean.rewind(Money.TEN);

		assertEquals(Money.ZERO, paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: a payment mean with an accumulated amount
	 * WHEN: trying to rewind more than accumulated
	 * THEN: an IllegalStateException is thrown and accumulated is unchanged
	 */
	@Test
	void testCannotRewindMoreThanAccumulated() {
		cash.charge(Money.TEN);

		assertThrows(
				IllegalStateException.class,
				() -> cash.rewind(Money.of("10.01"))
			);

		assertEquals(Money.TEN, cash.getAccumulated());
	}

	/**
	 * GIVEN: a payment mean
	 * WHEN: trying to rewind a null amount
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNullAmountIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> cash.rewind(null));
	}

	/**
	 * GIVEN: a payment mean
	 * WHEN: trying to rewind a zero or negative amount
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNonPositiveAmountIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> cash.rewind(Money.ZERO));
		assertThrows(
				IllegalArgumentException.class,
				() -> cash.rewind(Money.of("-0.01"))
			);
	}

	private static class ControllablePaymentMean extends PaymentMean {
		private boolean chargeable = true;

		@Override
		public boolean canBeCharged(Money amount) {
			return chargeable;
		}
	}
}
