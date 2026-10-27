package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Subtracting a smaller amount returns a positive difference
 * - Subtracting a larger amount returns a negative difference
 * - Subtracting a negative amount is equivalent to adding it
 * - Subtracting high-resolution amounts is exact
 */
class MoneyCalculatorSubtractTests {

	/**
	 * GIVEN: a calculator initialized with an amount
	 * WHEN: subtracting a smaller amount
	 * THEN: the result is the positive difference
	 */
	@Test
	void testSubtractSmallerAmount() {
		Money result = Money.calc(Money.of("12.34"))
				.subtract(Money.of("5.67"))
				.toMoneyExact();

		assertEquals(Money.of("6.67"), result);
	}

	/**
	 * GIVEN: a calculator initialized with an amount
	 * WHEN: subtracting a larger amount
	 * THEN: the result is the negative difference
	 */
	@Test
	void testSubtractLargerAmount() {
		Money result = Money.calc(Money.of("5.67"))
				.subtract(Money.of("12.34"))
				.toMoneyExact();

		assertEquals(Money.of("-6.67"), result);
	}

	/**
	 * GIVEN: a calculator initialized with a positive amount
	 * WHEN: subtracting a negative amount
	 * THEN: the absolute value of that amount is added
	 */
	@Test
	void testSubtractNegativeAmount() {
		Money result = Money.calc(Money.of("12.34"))
				.subtract(Money.of("-5.67"))
				.toMoneyExact();

		assertEquals(Money.of("18.01"), result);
	}

	/**
	 * GIVEN: two amounts with arbitrary precision
	 * WHEN: subtracting them
	 * THEN: the result is exact
	 */
	@Test
	void testSubtractWithArbitraryPrecision() {
		BigDecimal first = new BigDecimal("12345678901234567890123456789012345.123456789");
		BigDecimal second = new BigDecimal("0.000000001");

		Money result = Money.calc(Money.of(first))
				.subtract(Money.of(second))
				.toMoneyExact();

		assertEquals(first.subtract(second), result.amount());
	}
}
