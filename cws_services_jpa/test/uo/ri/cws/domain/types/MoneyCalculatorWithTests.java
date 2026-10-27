package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Applying a percentage increases the amount by that percentage
 * - Applying zero percent leaves the amount unchanged
 * - Applying a high-resolution percentage is exact
 */
class MoneyCalculatorWithTests {

	/**
	 * GIVEN: an amount and a percentage
	 * WHEN: applying the percentage with with()
	 * THEN: the amount is increased by that percentage
	 */
	@Test
	void testWithPercentage() {
		Money result = Money.calc(Money.of("100"))
				.with(Percentage.ofPercent("21"))
				.toMoneyExact();

		assertEquals(Money.of("121.00"), result);
	}

	/**
	 * GIVEN: an amount and a zero percentage
	 * WHEN: applying the percentage with with()
	 * THEN: the amount remains unchanged
	 */
	@Test
	void testWithZeroPercentage() {
		Money result = Money.calc(Money.of("123.456"))
				.with(Percentage.ofPercent("0"))
				.toMoneyExact();

		assertEquals(Money.of("123.456"), result);
	}

	/**
	 * GIVEN: an amount and a percentage with more than two decimal places
	 * WHEN: applying the percentage with with()
	 * THEN: the result is calculated exactly without intermediate rounding
	 */
	@Test
	void testWithHighResolutionPercentage() {
		Money result = Money.calc(Money.of("80"))
				.with(Percentage.ofPercent("12.345"))
				.toMoneyExact();

		assertEquals(Money.of("89.87600"), result);
	}
}
