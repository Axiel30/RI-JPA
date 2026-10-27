package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Multiplying by a BigDecimal is exact
 * - Multiplying by a String uses its exact decimal representation
 * - Multiplying by a long is exact
 * - Multiplying by a double follows its decimal value
 * - Multiplying by a percentage applies its ratio
 * - Multiplying by zero returns zero
 * - Multiplying by a negative factor changes the sign
 */
class MoneyCalculatorTimesTests {

	/**
	 * GIVEN: an amount and a high-resolution BigDecimal factor
	 * WHEN: multiplying the amount by the factor
	 * THEN: the result matches exact BigDecimal multiplication
	 */
	@Test
	void testTimesBigDecimalIsExact() {
		BigDecimal amount = new BigDecimal("12345678901234567890.123456789");
		BigDecimal factor = new BigDecimal("3.0000000001");

		Money result = Money.calc(Money.of(amount)).times(factor).toMoneyExact();

		assertEquals(amount.multiply(factor), result.amount());
	}

	/**
	 * GIVEN: an amount and a factor represented as a String
	 * WHEN: multiplying the amount by the factor
	 * THEN: the exact decimal representation of the String is used
	 */
	@Test
	void testTimesString() {
		Money result = Money.calc(Money.of("100")).times("1.21").toMoneyExact();

		assertEquals(Money.of("121.00"), result);
	}

	/**
	 * GIVEN: an amount and an integral factor
	 * WHEN: multiplying the amount by the long factor
	 * THEN: the result is exact
	 */
	@Test
	void testTimesLong() {
		Money result = Money.calc(Money.of("12.345")).times(3L).toMoneyExact();

		assertEquals(Money.of("37.035"), result);
	}

	/**
	 * GIVEN: an amount and a decimal factor represented as a double
	 * WHEN: multiplying the amount by the double factor
	 * THEN: the result follows the decimal value written by the caller
	 */
	@Test
	void testTimesDouble() {
		Money result = Money.calc(Money.of("100")).times(1.21).toMoneyExact();

		assertEquals(Money.of("121.00"), result);
	}

	/**
	 * GIVEN: an amount and a percentage
	 * WHEN: multiplying the amount by the percentage
	 * THEN: the percentage ratio is applied exactly
	 */
	@Test
	void testTimesPercentage() {
		Money result = Money.calc(Money.of("100"))
				.times(Percentage.ofPercent("21.125"))
				.toMoneyExact();

		assertEquals(Money.of("21.12500"), result);
	}

	/**
	 * GIVEN: an amount
	 * WHEN: multiplying it by zero
	 * THEN: the result is zero
	 */
	@Test
	void testTimesZero() {
		Money result = Money.calc(Money.of("123.456")).times(BigDecimal.ZERO).toMoneyExact();

		assertEquals(Money.ZERO, result);
	}

	/**
	 * GIVEN: a positive amount
	 * WHEN: multiplying it by a negative factor
	 * THEN: the result is negative
	 */
	@Test
	void testTimesNegativeFactor() {
		Money result = Money.calc(Money.of("12.34")).times("-2.5").toMoneyExact();

		assertEquals(Money.of("-30.850"), result);
	}
}
