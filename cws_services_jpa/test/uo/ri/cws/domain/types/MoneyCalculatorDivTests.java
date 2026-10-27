package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - A terminating division is exact
 * - A terminating result with more than 34 digits remains exact
 * - A non-terminating division uses DECIMAL128
 * - Division by zero throws ArithmeticException
 * - Dividing by a String uses its exact decimal representation
 * - Dividing by a long is exact
 * - Dividing by a double follows its decimal value
 * - Dividing by a negative divisor changes the sign
 */
class MoneyCalculatorDivTests {

	/**
	 * GIVEN: an amount and a divisor producing a terminating decimal expansion
	 * WHEN: dividing the amount
	 * THEN: the exact result is obtained
	 */
	@Test
	void testTerminatingDivisionIsExact() {
		Money result = Money.calc(Money.of("1")).div(new BigDecimal("8")).toMoneyExact();

		assertEquals(new BigDecimal("0.125"), result.amount());
	}

	/**
	 * GIVEN: a terminating division whose result has more than 34 significant digits
	 * WHEN: dividing the amount
	 * THEN: no DECIMAL128 rounding is applied
	 */
	@Test
	void testTerminatingDivisionKeepsArbitraryPrecision() {
		BigDecimal amount = new BigDecimal("8888888888888888888888888888888888888888"); // 40 digits > 34 of DECIMAL128
		BigDecimal expected = amount.divide(new BigDecimal("2"));
		
		Money result = Money.calc(Money.of(amount))
				.div(new BigDecimal("2"))
				.toMoneyExact();

		assertEquals(expected, result.amount());
	}

	/**
	 * GIVEN: an amount and a divisor producing a non-terminating decimal expansion
	 * WHEN: dividing the amount
	 * THEN: the result uses DECIMAL128 precision and HALF_EVEN
	 */
	@Test
	void testNonTerminatingDivisionUsesDecimal128() {
		BigDecimal expected = BigDecimal.ONE.divide(new BigDecimal("3"), MathContext.DECIMAL128);

		Money result = Money.calc(Money.of("1"))
				.div(new BigDecimal("3"))
				.toMoneyExact();

		assertEquals(expected, result.amount());
	}

	/**
	 * GIVEN: an amount
	 * WHEN: dividing it by zero
	 * THEN: an ArithmeticException is thrown
	 */
	@Test
	void testDivisionByZero() {
		assertThrows(ArithmeticException.class, () -> Money.calc(Money.ONE).div(BigDecimal.ZERO));
	}

	/**
	 * GIVEN: an amount and a divisor represented as a String
	 * WHEN: dividing the amount by the String divisor
	 * THEN: the exact decimal representation of the String is used
	 */
	@Test
	void testDivString() {
		Money result = Money.calc(Money.of("100")).div("1.25").toMoneyExact();

		assertEquals(Money.of("80"), result);
	}

	/**
	 * GIVEN: an amount and an integral divisor
	 * WHEN: dividing the amount by the long divisor
	 * THEN: the result is exact
	 */
	@Test
	void testDivLong() {
		Money result = Money.calc(Money.of("100")).div(5L).toMoneyExact();

		assertEquals(Money.of("20"), result);
	}

	/**
	 * GIVEN: an amount and a decimal divisor represented as a double
	 * WHEN: dividing the amount by the double divisor
	 * THEN: the result follows the decimal value written by the caller
	 */
	@Test
	void testDivDouble() {
		Money result = Money.calc(Money.of("100")).div(1.21).toMoneyExact();
		BigDecimal expected = new BigDecimal("100").divide(new BigDecimal("1.21"), MathContext.DECIMAL128);

		assertEquals(expected, result.amount());
	}

	/**
	 * GIVEN: a positive amount
	 * WHEN: dividing it by a negative divisor
	 * THEN: the result is negative
	 */
	@Test
	void testDivNegativeDivisor() {
		Money result = Money.calc(Money.of("10")).div(new BigDecimal("-4")).toMoneyExact();

		assertEquals(Money.of("-2.5"), result);
	}
}
