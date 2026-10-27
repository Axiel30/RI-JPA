package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Adding an amount to an empty calculator starts from zero
 * - Adding two positive amounts returns their sum
 * - Adding a negative amount is equivalent to subtracting it
 * - Adding high-resolution amounts is exact
 */
class MoneyCalculatorAddTests {

	/**
	 * GIVEN: an empty calculator
	 * WHEN: adding an amount
	 * THEN: the result is that amount
	 */
	@Test
	void testAddToEmptyCalculator() {
		Money result = Money.calc().add(Money.of("12.345")).toMoneyExact();

		assertEquals(Money.of("12.345"), result);
	}

	/**
	 * GIVEN: a calculator initialized with a positive amount
	 * WHEN: adding another positive amount
	 * THEN: the result is their sum
	 */
	@Test
	void testAddPositiveAmounts() {
		Money result = Money.calc(Money.of("12.34"))
				.add(Money.of("5.67"))
				.toMoneyExact();

		assertEquals(Money.of("18.01"), result);
	}

	/**
	 * GIVEN: a calculator initialized with a positive amount
	 * WHEN: adding a negative amount
	 * THEN: the result is reduced by that amount
	 */
	@Test
	void testAddNegativeAmount() {
		Money result = Money.calc(Money.of("12.34"))
				.add(Money.of("-5.67"))
				.toMoneyExact();

		assertEquals(Money.of("6.67"), result);
	}

	/**
	 * GIVEN: two amounts whose sum has more than 34 significant digits
	 * WHEN: adding them
	 * THEN: the result is exact
	 */
	@Test
	void testAddWithArbitraryPrecision() {
		BigDecimal first = new BigDecimal("12345678901234567890123456789012345.123456789");
		BigDecimal second = new BigDecimal("0.000000001");
		BigDecimal expected = first.add(second);

		Money result = Money.calc(Money.of(first))
				.add(Money.of(second))
				.toMoneyExact();

		assertEquals(expected, result.amount());
	}
}
