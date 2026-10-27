package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Negating a positive amount returns its negative counterpart
 * - Negating a negative amount returns its positive counterpart
 * - Negating zero returns zero
 * - Negating an amount preserves its resolution
 */
class MoneyCalculatorNegateTests {

	/**
	 * GIVEN: a positive amount
	 * WHEN: negating it
	 * THEN: the result is its negative counterpart
	 */
	@Test
	void testNegatePositiveAmount() {
		Money result = Money.calc(Money.of("12.34")).negate().toMoneyExact();

		assertEquals(Money.of("-12.34"), result);
	}

	/**
	 * GIVEN: a negative amount
	 * WHEN: negating it
	 * THEN: the result is its positive counterpart
	 */
	@Test
	void testNegateNegativeAmount() {
		Money result = Money.calc(Money.of("-12.34")).negate().toMoneyExact();

		assertEquals(Money.of("12.34"), result);
	}

	/**
	 * GIVEN: a zero amount
	 * WHEN: negating it
	 * THEN: the result remains zero
	 */
	@Test
	void testNegateZero() {
		Money result = Money.calc(Money.ZERO).negate().toMoneyExact();

		assertEquals(Money.ZERO, result);
	}

	/**
	 * GIVEN: an amount with high decimal resolution
	 * WHEN: negating it
	 * THEN: its scale is preserved
	 */
	@Test
	void testNegatePreservesResolution() {
		Money result = Money.calc(Money.of("12.3456789000")).negate().toMoneyExact();

		assertEquals(Money.of("-12.3456789000").amount(), result.amount());
	}
}
