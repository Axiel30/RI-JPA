package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;

/**
 * Scenarios:
 * - Spare part code cannot be null, empty or blank
 * - Spare part description cannot be null, empty or blank
 * - Cannot be negative values for price, stock, minStock or maxStock
 * - Spare part price is rounded to cents using HALF_EVEN
 */
class SparePartConstructorTests {

	/**
	 * WHEN: we create a spare part with null, empty or blank code
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testCodeNull() {
		assertThrows(IllegalArgumentException.class, () -> new SparePart(null));
		assertThrows(IllegalArgumentException.class, () -> new SparePart(""));
		assertThrows(IllegalArgumentException.class, () -> new SparePart(" "));
	}

	/**
	 * WHEN: we create a spare part with null, empty or blank description 
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testDescriptionCannotBeNull() {
		assertThrows(IllegalArgumentException.class, () -> new SparePart("code", null, Money.ZERO));
		assertThrows(IllegalArgumentException.class, () -> new SparePart("code", "", Money.ZERO));
		assertThrows(IllegalArgumentException.class, () -> new SparePart("code", "  ", Money.ZERO));
	}

	/**
	 * WHEN: we create a spare part with negative price, stock, minStock or
	 * maxStock THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testPriceCannotBeNgative() {
		assertThrows(IllegalArgumentException.class, () -> new SparePart("code", "description", Money.of("-1.0")));
	}

	/**
	 * GIVEN: a price whose resolution is higher than cents
	 * WHEN: we create a spare part with that price
	 * THEN: its price is rounded to cents using HALF_EVEN
	 */
	@Test
	void testPriceIsRoundedToCentsUsingHalfEven() {
		SparePart sparePart = new SparePart("code", "description", Money.of("10.015"));

		assertEquals(Money.of("10.02").amount(), sparePart.getPrice().amount());
	}
}
