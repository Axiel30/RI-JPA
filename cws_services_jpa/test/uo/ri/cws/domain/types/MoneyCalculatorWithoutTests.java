package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Removing a percentage recovers the amount before that percentage
 * - Removing zero percent leaves the amount unchanged
 * - Removing a percentage uses DECIMAL128 when the division is non-terminating
 */
class MoneyCalculatorWithoutTests {

	/**
	 * GIVEN: an amount that includes a percentage
	 * WHEN: removing that percentage with without()
	 * THEN: the original amount is recovered exactly
	 */
	@Test
	void testWithoutPercentage() {
		Money result = Money.calc(Money.of("121"))
				.without(Percentage.ofPercent("21"))
				.toMoneyExact();

		assertEquals(Money.of("100"), result);
	}

	/**
	 * GIVEN: an amount and a zero percentage
	 * WHEN: removing the percentage with without()
	 * THEN: the amount remains unchanged
	 */
	@Test
	void testWithoutZeroPercentage() {
		Money result = Money.calc(Money.of("123.456"))
				.without(Percentage.ofPercent("0"))
				.toMoneyExact();

		assertEquals(Money.of("123.456"), result);
	}

	/**
	 * GIVEN: an amount whose division by a percentage multiplier does not terminate
	 * WHEN: removing that percentage with without()
	 * THEN: the result uses DECIMAL128 precision
	 */
	@Test
	void testWithoutPercentageUsesDecimal128WhenNeeded() {
		Money result = Money.calc(Money.of("100"))
				.without(Percentage.ofPercent("21"))
				.toMoneyExact();
		BigDecimal expected = new BigDecimal("100").divide(new BigDecimal("1.21"), MathContext.DECIMAL128);

		assertEquals(expected, result.amount());
	}
}
