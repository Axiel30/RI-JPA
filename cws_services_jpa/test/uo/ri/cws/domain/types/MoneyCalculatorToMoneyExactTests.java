package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - toMoneyExact() preserves the current value and scale
 * - toMoneyExact() preserves arbitrary precision
 * - A materialized Money is unaffected by later calculator operations
 * - toMoneyExact() creates a new Money value
 */
class MoneyCalculatorToMoneyExactTests {

	/**
	 * GIVEN: a calculation with more than two decimal places
	 * WHEN: materializing it with toMoneyExact()
	 * THEN: its value and scale are preserved
	 */
	@Test
	void testToMoneyExactPreservesValueAndScale() {
		Money result = Money.calc(Money.of("10.00500")).toMoneyExact();

		assertEquals(new BigDecimal("10.00500"), result.amount());
	}

	/**
	 * GIVEN: a calculation with more than 34 significant digits
	 * WHEN: materializing it with toMoneyExact()
	 * THEN: all its digits are preserved
	 */
	@Test
	void testToMoneyExactPreservesArbitraryPrecision() {
		BigDecimal amount = new BigDecimal("12345678901234567890123456789012345.123456789");

		Money result = Money.calc(Money.of(amount)).toMoneyExact();

		assertEquals(amount, result.amount());
	}

	/**
	 * GIVEN: a Money already materialized from a calculator
	 * WHEN: performing another operation on that calculator
	 * THEN: the previously materialized Money remains unchanged
	 */
	@Test
	void testMaterializedMoneyIsIndependentFromCalculator() {
		Money.MoneyCalculator calculator = Money.calc(Money.TEN);
		Money firstResult = calculator.toMoneyExact();

		calculator.add(Money.ONE);

		assertEquals(Money.TEN, firstResult);
		assertEquals(Money.of("11"), calculator.toMoneyExact());
	}

	/**
	 * GIVEN: a Money used to initialize a calculator
	 * WHEN: materializing the calculator with toMoneyExact()
	 * THEN: a different Money instance with the same value is returned
	 */
	@Test
	void testToMoneyExactCreatesNewMoney() {
		Money initial = Money.of("10.00");

		Money result = Money.calc(initial).toMoneyExact();

		assertNotSame(initial, result);
		assertEquals(initial, result);
	}
}
