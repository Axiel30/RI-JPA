package uo.ri.cws.domain.types;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Allocating an evenly divisible positive amount returns equal shares
 * - Allocating a positive amount distributes remaining cents without loss
 * - Allocating fewer cents than targets assigns one cent to the first targets
 * - Allocating to one target returns the rounded amount
 * - Allocating zero returns zero shares
 * - Allocating rounds the source amount to cents using HALF_EVEN
 * - Allocating an evenly divisible negative amount returns equal shares
 * - Allocating a negative amount distributes negative remaining cents without loss
 */
class MoneyCalculatorAllocateTests {

	/**
	 * GIVEN: a positive amount evenly divisible in cents by the number of targets
	 * WHEN: allocating the amount
	 * THEN: every target receives the same share
	 */
	@Test
	void testAllocateEvenlyDivisiblePositiveAmount() {
		Money[] result = Money.calc(Money.of("10.02")).allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("3.34"), Money.of("3.34"), Money.of("3.34") },
				result
		);
	}

	/**
	 * GIVEN: a positive amount not evenly divisible in cents by the number of targets
	 * WHEN: allocating the amount
	 * THEN: remaining cents are assigned to the first targets without loss
	 */
	@Test
	void testAllocatePositiveAmountWithRemainder() {
		Money[] result = Money.calc(Money.of("10.00")).allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("3.34"), Money.of("3.33"), Money.of("3.33") },
				result
		);
		assertEquals(Money.of("10.00"), sum(result));
	}

	/**
	 * GIVEN: a positive amount containing fewer cents than targets
	 * WHEN: allocating the amount
	 * THEN: the first targets receive one cent and the others receive zero
	 */
	@Test
	void testAllocateFewerCentsThanTargets() {
		Money[] result = Money.calc(Money.of("0.02")).allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("0.01"), Money.of("0.01"), Money.ZERO },
				result
		);
	}

	/**
	 * GIVEN: an amount with more than two decimal places
	 * WHEN: allocating it to one target
	 * THEN: the target receives the amount rounded to cents
	 */
	@Test
	void testAllocateToOneTarget() {
		Money[] result = Money.calc(Money.of("10.015")).allocate(1);

		assertArrayEquals(new Money[] { Money.of("10.02") }, result);
	}

	/**
	 * GIVEN: a zero amount
	 * WHEN: allocating it among several targets
	 * THEN: every target receives zero
	 */
	@Test
	void testAllocateZero() {
		Money[] result = Money.calc(Money.ZERO).allocate(3);

		assertArrayEquals(new Money[] { Money.ZERO, Money.ZERO, Money.ZERO }, result);
	}

	/**
	 * GIVEN: an amount exactly halfway between two cents whose even neighbour is higher
	 * WHEN: allocating the amount
	 * THEN: the source is rounded with HALF_EVEN before being divided
	 */
	@Test
	void testAllocateRoundsSourceUsingHalfEven() {
		Money[] result = Money.calc(Money.of("10.015")).allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("3.34"), Money.of("3.34"), Money.of("3.34") },
				result
		);
	}

	/**
	 * GIVEN: a negative amount evenly divisible in cents by the number of targets
	 * WHEN: allocating the amount
	 * THEN: every target receives the same negative share
	 */
	@Test
	void testAllocateEvenlyDivisibleNegativeAmount() {
		Money[] result = Money.calc(Money.of("-10.02")).allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("-3.34"), Money.of("-3.34"), Money.of("-3.34") },
				result
		);
	}

	/**
	 * GIVEN: a negative amount not evenly divisible in cents by the number of targets
	 * WHEN: allocating the amount
	 * THEN: negative remaining cents are assigned to the first targets without loss
	 */
	@Test
	void testAllocateNegativeAmountWithRemainder() {
		Money[] result = Money.calc(Money.of("-10.00"))
				.allocate(3);

		assertArrayEquals(
				new Money[] { Money.of("-3.34"), Money.of("-3.33"), Money.of("-3.33") },
				result
		);
		assertEquals(Money.of("-10.00"), sum(result));
	}

	private Money sum(Money[] amounts) {
		Money.MoneyCalculator calculator = Money.calc();
		for (Money amount : amounts) {
			calculator.add(amount);
		}
		return calculator.toMoneyExact();
	}
}
