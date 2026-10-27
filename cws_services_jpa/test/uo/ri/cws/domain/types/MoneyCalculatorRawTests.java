package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - raw() returns zero for an empty calculator
 * - raw() returns the current exact calculation without materializing Money
 * - raw() does not round to cents
 */
class MoneyCalculatorRawTests {

	/**
	 * GIVEN: an empty calculator
	 * WHEN: obtaining its raw value
	 * THEN: zero is returned
	 */
	@Test
	void testRawOnEmptyCalculator() {
		BigDecimal result = Money.calc().raw();

		assertEquals(BigDecimal.ZERO, result);
	}

	/**
	 * GIVEN: a calculator after several exact operations
	 * WHEN: obtaining its raw value
	 * THEN: the current exact BigDecimal is returned
	 */
	@Test
	void testRawReturnsCurrentCalculation() {
		BigDecimal result = Money.calc(Money.of("10.005"))
				.add(Money.of("0.0001"))
				.times("2")
				.raw();

		assertEquals(new BigDecimal("20.0102"), result);
	}

	/**
	 * GIVEN: a calculation with more than two decimal places
	 * WHEN: obtaining its raw value
	 * THEN: no rounding to cents is applied
	 */
	@Test
	void testRawDoesNotRound() {
		BigDecimal result = Money.calc(Money.of("10.005")).raw();

		assertEquals(new BigDecimal("10.005"), result);
	}
}
