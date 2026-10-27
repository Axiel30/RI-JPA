package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Removing substitutions from an empty intervention does nothing
 * - Removing substitutions unlinks the substitution from both ends
 * - Removing substitutions unlinks every substitution
 */
class InterventionRemoveSubstitutionsTests {

	private Intervention intervention;
	private SparePart firstSparePart;
	private SparePart secondSparePart;

	@BeforeEach
	void setUp() {
		WorkOrder workOrder = new WorkOrder(
				new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza")
			);
		Mechanic mechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
		intervention = new Intervention(mechanic, workOrder, 0);
		firstSparePart = new SparePart("R1001", "first spare", Money.of("10.00"));
		secondSparePart = new SparePart("R1002", "second spare", Money.of("20.00"));
	}

	/**
	 * GIVEN: an intervention without substitutions
	 * WHEN: its substitutions are removed
	 * THEN: its collection remains empty
	 */
	@Test
	void testEmptyIntervention() {
		intervention.removeSubstitutions();

		assertTrue(intervention.getSubstitutions().isEmpty());
	}

	/**
	 * GIVEN: an intervention with one substitution
	 * WHEN: its substitutions are removed
	 * THEN: the substitution is unlinked from the intervention and spare part
	 */
	@Test
	void testRemoveOneSubstitution() {
		Substitution substitution = linkedSubstitution(firstSparePart);

		intervention.removeSubstitutions();

		assertTrue(intervention.getSubstitutions().isEmpty());
		assertNull(substitution.getIntervention());
		assertNull(substitution.getSparePart());
	}

	/**
	 * GIVEN: an intervention with several substitutions
	 * WHEN: its substitutions are removed
	 * THEN: every substitution is unlinked from both ends
	 */
	@Test
	void testRemoveSeveralSubstitutions() {
		Substitution first = linkedSubstitution(firstSparePart);
		Substitution second = linkedSubstitution(secondSparePart);

		intervention.removeSubstitutions();

		assertTrue(intervention.getSubstitutions().isEmpty());
		assertNull(first.getIntervention());
		assertNull(first.getSparePart());
		assertNull(second.getIntervention());
		assertNull(second.getSparePart());
	}

	private Substitution linkedSubstitution(SparePart sparePart) {
		Substitution substitution = DomainTestMocksFactory.oneSubstitution();
		Associations.PerformsSubstitutes.atomicLink(
				intervention,
				substitution,
				sparePart
			);
		return substitution;
	}
}
