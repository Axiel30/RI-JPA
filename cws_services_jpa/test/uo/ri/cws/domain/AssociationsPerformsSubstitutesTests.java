package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Creating a substitution atomically links it with its intervention and spare part
 * - Atomically unlinking a substitution removes all its association ends
 * - The collection returned by Intervention.getSubstitutions() is a safe copy
 * - Atomic linking rejects null arguments
 * - Atomic linking rejects a substitution that already has an intervention
 * - Atomic linking rejects a substitution that already has a spare part
 * - Atomic linking rejects a substitution already present in the intervention
 * - Atomic unlinking rejects a null substitution
 * - Atomic unlinking rejects a substitution without an intervention
 * - Atomic unlinking rejects a substitution without a spare part
 * - Atomic unlinking rejects an inconsistent inverse end
 */
class AssociationsPerformsSubstitutesTests {

	private Intervention intervention;
	private SparePart sparePart;
	private Substitution substitution;

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
		sparePart = new SparePart("R1001", "junta la trocla", Money.of("100.00"));
		substitution = DomainTestMocksFactory.oneSubstitution();
	}

	/**
	 * GIVEN: an unlinked intervention, substitution and spare part
	 * WHEN: the substitution is atomically linked
	 * THEN: it is added to the intervention
	 * AND: both ends are set on the substitution
	 */
	@Test
	void testSubstitutionAtomicallyLinked() {
		Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart);

		assertTrue(intervention.getSubstitutions().contains(substitution));
		assertSame(intervention, substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
	}

	/**
	 * GIVEN: a substitution linked to an intervention and a spare part
	 * WHEN: the substitution is atomically unlinked
	 * THEN: it is removed from the intervention
	 * AND: both ends are cleared on the substitution
	 */
	@Test
	void testSubstitutionAtomicallyUnlinked() {
		Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart);

		Associations.PerformsSubstitutes.atomicUnlink(substitution);

		assertFalse(intervention.getSubstitutions().contains(substitution));
		assertTrue(intervention.getSubstitutions().isEmpty());
		assertNull(substitution.getIntervention());
		assertNull(substitution.getSparePart());
	}

	/**
	 * GIVEN: an intervention with a linked substitution
	 * WHEN: the substitution is removed from the returned collection
	 * THEN: the intervention's internal collection remains unchanged
	 */
	@Test
	void testSafeReturnOnIntervention() {
		Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart);

		Set<Substitution> substitutions = intervention.getSubstitutions();
		substitutions.remove(substitution);

		assertTrue(substitutions.isEmpty());
		assertTrue(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: unlinked association ends
	 * WHEN: atomicLink is called with each null argument
	 * THEN: an IllegalArgumentException is thrown every time
	 * AND: the association remains unchanged
	 */
	@Test
	void testAtomicLinkNullArguments() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(null, substitution, sparePart)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(intervention, null, sparePart)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(intervention, substitution, null)
		);

		assertNull(substitution.getIntervention());
		assertNull(substitution.getSparePart());
		assertFalse(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: a substitution already linked to an intervention
	 * WHEN: linking it to another intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testAtomicLinkSubstitutionWithIntervention() {
		WorkOrder workOrder = new WorkOrder(
				new Vehicle(new LicensePlate("5678 KLM"), "seat", "leon")
			);
		Mechanic mechanic = new Mechanic(
				new Nif("87654321X"),
				new FullName("Another", "Mechanic")
			);
		Intervention another = new Intervention(mechanic, workOrder, 0);
		Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(another, substitution, sparePart)
		);

		assertSame(intervention, substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
		assertTrue(intervention.getSubstitutions().contains(substitution));
		assertTrue(another.getSubstitutions().isEmpty());
	}

	/**
	 * GIVEN: a substitution whose spare-part end is already set
	 * WHEN: atomically linking the substitution
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkSubstitutionWithSparePart() {
		// Test-only backdoor: creates an inconsistent association end.
		substitution._setSparePart(sparePart);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart)
		);

		assertNull(substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
		assertFalse(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: a substitution present in an intervention without its own ends set
	 * WHEN: atomically linking the substitution
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkSubstitutionAlreadyInIntervention() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		intervention._getSubstitutions().add(substitution);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicLink(intervention, substitution, sparePart)
		);

		assertNull(substitution.getIntervention());
		assertNull(substitution.getSparePart());
		assertTrue(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: no substitution
	 * WHEN: atomicUnlink is called with null
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the association remains empty
	 */
	@Test
	void testAtomicUnlinkNullSubstitution() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.PerformsSubstitutes.atomicUnlink(null)
		);

		assertNull(substitution.getIntervention());
		assertNull(substitution.getSparePart());
		assertTrue(intervention.getSubstitutions().isEmpty());
	}

	/**
	 * GIVEN: a substitution with a spare part but no intervention
	 * WHEN: atomically unlinking the substitution
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkSubstitutionWithoutIntervention() {
		// Test-only backdoor: creates an inconsistent association end.
		substitution._setSparePart(sparePart);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicUnlink(substitution)
		);

		assertNull(substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
	}

	/**
	 * GIVEN: a substitution linked to an intervention but without a spare part
	 * WHEN: atomically unlinking the substitution
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkSubstitutionWithoutSparePart() {
		// Test-only backdoors: create an inconsistent association deliberately.
		substitution._setIntervention(intervention);
		intervention._getSubstitutions().add(substitution);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicUnlink(substitution)
		);

		assertSame(intervention, substitution.getIntervention());
		assertNull(substitution.getSparePart());
		assertTrue(intervention.getSubstitutions().contains(substitution));
	}

	/**
	 * GIVEN: a substitution whose ends are set but which is absent from the intervention
	 * WHEN: atomically unlinking the substitution
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkInconsistentIntervention() {
		// Test-only backdoors: create an inconsistent association deliberately.
		substitution._setIntervention(intervention);
		substitution._setSparePart(sparePart);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.PerformsSubstitutes.atomicUnlink(substitution)
		);

		assertSame(intervention, substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
		assertFalse(intervention.getSubstitutions().contains(substitution));
	}
}
