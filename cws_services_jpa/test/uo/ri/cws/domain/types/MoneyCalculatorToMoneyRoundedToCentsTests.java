package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Materializing an amount with fewer than two decimals produces scale two
 * - A non-tie is rounded to its nearest cent
 * - A positive tie is rounded down when the retained cent is even
 * - A positive tie is rounded up when the upper retained cent is even
 * - A negative tie is rounded towards zero when the retained cent is even
 * - A negative tie is rounded away from zero when the lower retained cent is even
 */
class MoneyCalculatorToMoneyRoundedToCentsTests {

	/**
	 * GIVEN: an amount with fewer than two decimal places
	 * WHEN: materializing it rounded to cents
	 * THEN: the resulting BigDecimal has scale two
	 */
	@Test
	void testToMoneyRoundedToCentsProducesScaleTwo() {
		Money result = Money.calc(Money.of("10")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("10.00"), result.amount());
	}

	/**
	 * GIVEN: an amount that is not exactly halfway between two cents
	 * WHEN: materializing it rounded to cents
	 * THEN: it is rounded to the nearest cent
	 */
	@Test
	void testToMoneyRoundedToCentsRoundsToNearestCent() {
		Money result = Money.calc(Money.of("10.006")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("10.01"), result.amount());
	}

	/**
	 * GIVEN: a positive tie whose lower cent has an even last digit
	 * WHEN: materializing it rounded to cents
	 * THEN: it is rounded down to the even cent
	 */
	@Test
	void testPositiveTieRoundsDownToEvenCent() {
		Money result = Money.calc(Money.of("10.005")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("10.00"), result.amount());
	}

	/**
	 * GIVEN: a positive tie whose upper cent has an even last digit
	 * WHEN: materializing it rounded to cents
	 * THEN: it is rounded up to the even cent
	 */
	@Test
	void testPositiveTieRoundsUpToEvenCent() {
		Money result = Money.calc(Money.of("10.015")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("10.02"), result.amount());
	}

	/**
	 * GIVEN: a negative tie whose cent towards zero has an even last digit
	 * WHEN: materializing it rounded to cents
	 * THEN: it is rounded towards zero to the even cent
	 */
	@Test
	void testNegativeTieRoundsTowardsZeroToEvenCent() {
		Money result = Money.calc(Money.of("-10.005")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("-10.00"), result.amount());
	}

	/**
	 * GIVEN: a negative tie whose cent away from zero has an even last digit
	 * WHEN: materializing it rounded to cents
	 * THEN: it is rounded away from zero to the even cent
	 */
	@Test
	void testNegativeTieRoundsAwayFromZeroToEvenCent() {
		Money result = Money.calc(Money.of("-10.015")).toMoneyRoundedToCents();

		assertEquals(new BigDecimal("-10.02"), result.amount());
	}
}
