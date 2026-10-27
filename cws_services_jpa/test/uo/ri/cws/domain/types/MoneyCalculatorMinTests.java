package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - min() returns the argument when it is smaller
 * - min() retains the current amount when it is smaller
 * - min() works with negative amounts
 * - min() preserves the current representation when values are equal
 */
class MoneyCalculatorMinTests {

	/**
	 * GIVEN: a current amount and a smaller argument
	 * WHEN: selecting their minimum
	 * THEN: the argument is returned
	 */
	@Test
	void testMinReturnsArgument() {
		Money result = Money.calc(Money.of("100")).min(Money.of("50")).toMoneyExact();

		assertEquals(Money.of("50"), result);
	}

	/**
	 * GIVEN: a current amount and a larger argument
	 * WHEN: selecting their minimum
	 * THEN: the current amount is retained
	 */
	@Test
	void testMinRetainsCurrentAmount() {
		Money result = Money.calc(Money.of("50")).min(Money.of("100")).toMoneyExact();

		assertEquals(Money.of("50"), result);
	}

	/**
	 * GIVEN: two negative amounts
	 * WHEN: selecting their minimum
	 * THEN: the most negative amount is returned
	 */
	@Test
	void testMinNegativeAmounts() {
		Money result = Money.calc(Money.of("-50")).min(Money.of("-100")).toMoneyExact();

		assertEquals(Money.of("-100"), result);
	}

	/**
	 * GIVEN: numerically equal amounts with different scales
	 * WHEN: selecting their minimum
	 * THEN: the current amount representation is retained
	 */
	@Test
	void testMinEqualValuesRetainsCurrentRepresentation() {
		Money result = Money.calc(Money.of("10.000")).min(Money.of("10.0")).toMoneyExact();

		assertEquals(Money.of("10.000").amount(), result.amount());
	}
}
