package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - A voucher can be created with zero initial available amount
 * - A voucher can be created with positive initial available amount
 * - Negative initial available amount is rejected by both public constructors
 */
class VoucherConstructorTests {

	/**
	 * GIVEN: a zero initial available amount
	 * WHEN: creating a voucher
	 * THEN: its available and accumulated amounts are zero
	 */
	@Test
	void testZeroInitialAvailableAmountIsAccepted() {
		Voucher voucher = new Voucher(new VoucherCode("V-1"), "Test voucher", Money.ZERO);

		assertEquals(Money.ZERO, voucher.getAvailableAmount());
		assertEquals(Money.ZERO, voucher.getAccumulated());
	}

	/**
	 * GIVEN: a positive initial available amount
	 * WHEN: creating a voucher
	 * THEN: its available amount is the initial amount and its accumulated amount is zero
	 */
	@Test
	void testPositiveInitialAvailableAmountIsAccepted() {
		Money initialAmount = Money.of("25.00");
		Voucher voucher = new Voucher(new VoucherCode("V-1"), "Test voucher", initialAmount);

		assertEquals(initialAmount, voucher.getAvailableAmount());
		assertEquals(Money.ZERO, voucher.getAccumulated());
	}

	/**
	 * GIVEN: a negative initial available amount
	 * WHEN: creating a voucher with an explicit description
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNegativeInitialAvailableAmountIsRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Voucher(new VoucherCode("V-1"), "Test voucher", Money.of("-0.01"))
			);
	}

	/**
	 * GIVEN: a negative initial available amount
	 * WHEN: creating a voucher with the default description
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNegativeInitialAvailableAmountIsRejectedWithDefaultDescription() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Voucher(new VoucherCode("V-1"), Money.of("-0.01"))
			);
	}
}
