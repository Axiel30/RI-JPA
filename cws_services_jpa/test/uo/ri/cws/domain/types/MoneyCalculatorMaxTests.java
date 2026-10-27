package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - max() returns the argument when it is larger
 * - max() retains the current amount when it is larger
 * - max() works with negative amounts
 * - max() preserves the current representation when values are equal
 */
class MoneyCalculatorMaxTests {

	/**
	 * GIVEN: a current amount and a larger argument
	 * WHEN: selecting their maximum
	 * THEN: the argument is returned
	 */
	@Test
	void testMaxReturnsArgument() {
		Money result = Money.calc(Money.of("50")).max(Money.of("100")).toMoneyExact();

		assertEquals(Money.of("100"), result);
	}

	/**
	 * GIVEN: a current amount and a smaller argument
	 * WHEN: selecting their maximum
	 * THEN: the current amount is retained
	 */
	@Test
	void testMaxRetainsCurrentAmount() {
		Money result = Money.calc(Money.of("100")).max(Money.of("50")).toMoneyExact();

		assertEquals(Money.of("100"), result);
	}

	/**
	 * GIVEN: two negative amounts
	 * WHEN: selecting their maximum
	 * THEN: the least negative amount is returned
	 */
	@Test
	void testMaxNegativeAmounts() {
		Money result = Money.calc(Money.of("-50")).max(Money.of("-100")).toMoneyExact();

		assertEquals(Money.of("-50"), result);
	}

	/**
	 * GIVEN: numerically equal amounts with different scales
	 * WHEN: selecting their maximum
	 * THEN: the current amount representation is retained
	 */
	@Test
	void testMaxEqualValuesRetainsCurrentRepresentation() {
		Money result = Money.calc(Money.of("10.000")).max(Money.of("10.0")).toMoneyExact();

		assertEquals(Money.of("10.000").amount(), result.amount());
	}
}
