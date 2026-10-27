package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;

/**
 * Scenarios:
 * - A substitution stores quantity and the spare part price at creation time
 * - A substitution computes its rounded total amount
 * - A substitution is linked bidirectionally with its intervention
 * - A substitution is linked unidirectionally with its spare part
 * - Zero and negative quantities are rejected
 * - Null intervention and spare part arguments are rejected
 */
class SubstitutionConstructorTests {

	private static final Money SPARE_PRICE = Money.of("100.005");

	private Intervention intervention;
	private SparePart sparePart;

	@BeforeEach
	void setUp() {
		intervention = DomainTestMocksFactory.oneIntervention();
		sparePart = new SparePart("R1001", "junta la trocla", SPARE_PRICE);
	}

	/**
	 * GIVEN: a spare part whose price is normalized to cents
	 * WHEN: a substitution is created
	 * THEN: it stores quantity and the spare price
	 * AND: its total amount is returned rounded to cents
	 * AND: all association ends are linked as designed
	 */
	@Test
	void testSubstitution() {
		Substitution substitution = new Substitution(sparePart, intervention, 2);

		assertEquals(2, substitution.getQuantity());
		assertEquals(Money.of("100.00"), substitution.getPrice());
		assertEquals(Money.of("200.00"), substitution.getTotalAmount());
		assertSame(intervention, substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
		assertTrue(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: a spare part and an intervention
	 * WHEN: a substitution is created with a zero or negative quantity
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNonPositiveQuantityIsRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Substitution(sparePart, intervention, 0)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Substitution(sparePart, intervention, -1)
			);
	}

	/**
	 * GIVEN: the arguments needed to create a substitution
	 * WHEN: intervention or spare part is null
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNullArgumentsAreRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Substitution(sparePart, null, 2)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Substitution(null, intervention, 2)
			);
	}

}
