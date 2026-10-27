package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - Charging accumulates the requested amount
 * - Successive charges are accumulated
 * - The amount is normalized to cents before validation and accumulation
 * - A payment mean that cannot be charged is left unchanged
 * - Null and non-positive amounts after normalization are rejected
 * - Charging a voucher also reduces its available amount
 * - A rejected voucher charge leaves both amounts unchanged
 */
class PaymentMeanChargeTests {

	/**
	 * GIVEN: a chargeable payment mean with no accumulated amount
	 * WHEN: charging a positive amount
	 * THEN: that amount is accumulated
	 */
	@Test
	void testChargeIncreasesAccumulatedAmount() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();

		paymentMean.charge(Money.of("40.00"));

		assertEquals(Money.of("40.00"), paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: a previously charged payment mean
	 * WHEN: charging it again
	 * THEN: both amounts are accumulated
	 */
	@Test
	void testSuccessiveChargesAreAccumulated() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();
		paymentMean.charge(Money.of("40.00"));

		paymentMean.charge(Money.of("2.34"));

		assertEquals(Money.of("42.34"), paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: an amount with more than two decimal places
	 * WHEN: charging it
	 * THEN: canBeCharged receives the normalized amount
	 * AND: the normalized amount is accumulated
	 */
	@Test
	void testAmountIsNormalizedBeforeValidationAndAccumulation() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();

		paymentMean.charge(Money.of("10.005"));

		assertEquals(Money.of("10.00"), paymentMean.checkedAmount);
		assertEquals(Money.of("10.00"), paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: a payment mean that cannot accept a charge
	 * WHEN: charging it
	 * THEN: an IllegalStateException is thrown
	 * AND: its accumulated amount remains unchanged
	 */
	@Test
	void testUnchargeablePaymentMeanIsUnchanged() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();
		paymentMean.chargeable = false;

		assertThrows(
				IllegalStateException.class,
				() -> paymentMean.charge(Money.TEN)
			);

		assertEquals(Money.ZERO, paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: a payment mean
	 * WHEN: charging null or an amount that is not positive after normalization
	 * THEN: an IllegalArgumentException is thrown
	 * AND: its accumulated amount remains unchanged
	 */
	@Test
	void testInvalidAmountsAreRejected() {
		ControllablePaymentMean paymentMean = new ControllablePaymentMean();

		assertThrows(IllegalArgumentException.class, () -> paymentMean.charge(null));
		assertThrows(
				IllegalArgumentException.class,
				() -> paymentMean.charge(Money.ZERO)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> paymentMean.charge(Money.of("-0.01"))
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> paymentMean.charge(Money.of("0.004"))
			);

		assertEquals(Money.ZERO, paymentMean.getAccumulated());
	}

	/**
	 * GIVEN: a voucher with an available amount
	 * WHEN: charging part of that amount
	 * THEN: the amount is accumulated
	 * AND: the available amount is reduced
	 */
	@Test
	void testVoucherChargeReducesAvailableAmount() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);

		voucher.charge(Money.of("40.00"));

		assertEquals(Money.of("40.00"), voucher.getAccumulated());
		assertEquals(Money.of("60.00"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: a voucher with insufficient available amount
	 * WHEN: trying to charge it
	 * THEN: an IllegalStateException is thrown
	 * AND: both monetary amounts remain unchanged
	 */
	@Test
	void testRejectedVoucherChargeLeavesAmountsUnchanged() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);

		assertThrows(
				IllegalStateException.class,
				() -> voucher.charge(Money.of("100.01"))
			);

		assertEquals(Money.ZERO, voucher.getAccumulated());
		assertEquals(Money.of("100.00"), voucher.getAvailableAmount());
	}

	private static class ControllablePaymentMean extends PaymentMean {
		private boolean chargeable = true;
		private Money checkedAmount;

		@Override
		public boolean canBeCharged(Money amount) {
			checkedAmount = amount;
			return chargeable;
		}
	}
}
