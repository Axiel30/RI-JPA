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
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Creating an intervention atomically links it with its work order and mechanic
 * - Atomically unlinking an intervention removes all its association ends
 * - The collection returned by WorkOrder.getInterventions() is a safe copy
 * - Atomic linking rejects null arguments
 * - Atomic linking rejects an intervention that already has a work order
 * - Atomic linking rejects an intervention that already has a mechanic
 * - Atomic linking rejects an intervention already present in the work order
 * - Atomic unlinking rejects a null intervention
 * - Atomic unlinking rejects an intervention without a work order
 * - Atomic unlinking rejects an intervention without a mechanic
 * - Atomic unlinking rejects an inconsistent inverse end
 */
class AssociationsRecordsIntervenesTests {

	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;

	@BeforeEach
	void setUp() {
		workOrder = new WorkOrder(
				new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza")
			);
		mechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
		intervention = DomainTestMocksFactory.oneIntervention();
	}

	/**
	 * GIVEN: an unlinked work order, intervention and mechanic
	 * WHEN: the intervention is atomically linked
	 * THEN: it is added to the work order
	 * AND: both ends are set on the intervention
	 */
	@Test
	void testInterventionAtomicallyLinked() {
		Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic);

		assertTrue(workOrder.getInterventions().contains(intervention));
		assertSame(workOrder, intervention.getWorkOrder());
		assertSame(mechanic, intervention.getMechanic());
	}

	/**
	 * GIVEN: an intervention linked to a work order and a mechanic
	 * WHEN: the intervention is atomically unlinked
	 * THEN: it is removed from the work order
	 * AND: both ends are cleared on the intervention
	 */
	@Test
	void testInterventionAtomicallyUnlinked() {
		Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic);

		Associations.RecordsIntervenes.atomicUnlink(intervention);

		assertFalse(workOrder.getInterventions().contains(intervention));
		assertTrue(workOrder.getInterventions().isEmpty());
		assertNull(intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
	}

	/**
	 * GIVEN: a work order with a linked intervention
	 * WHEN: the intervention is removed from the returned collection
	 * THEN: the work order's internal collection remains unchanged
	 */
	@Test
	void testSafeReturnOnWorkOrder() {
		Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic);

		Set<Intervention> interventions = workOrder.getInterventions();
		interventions.remove(intervention);

		assertTrue(interventions.isEmpty());
		assertTrue(workOrder.getInterventions().contains(intervention));
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
			() -> Associations.RecordsIntervenes.atomicLink(null, intervention, mechanic)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.RecordsIntervenes.atomicLink(workOrder, null, mechanic)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.RecordsIntervenes.atomicLink(workOrder, intervention, null)
		);

		assertNull(intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
		assertFalse(workOrder.getInterventions().contains(intervention));
	}

	/**
	 * GIVEN: an intervention already linked to a work order and mechanic
	 * WHEN: linking it to another work order
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testAtomicLinkInterventionWithWorkOrder() {
		WorkOrder another = new WorkOrder(
				new Vehicle(new LicensePlate("5678 KLM"), "seat", "leon")
			);
		Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicLink(another, intervention, mechanic)
		);

		assertSame(workOrder, intervention.getWorkOrder());
		assertSame(mechanic, intervention.getMechanic());
		assertTrue(workOrder.getInterventions().contains(intervention));
		assertTrue(another.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: an intervention whose mechanic end is already set
	 * WHEN: atomically linking the intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkInterventionWithMechanic() {
		// Test-only backdoor: creates an inconsistent association end.
		intervention._setMechanic(mechanic);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic)
		);

		assertNull(intervention.getWorkOrder());
		assertSame(mechanic, intervention.getMechanic());
		assertFalse(workOrder.getInterventions().contains(intervention));
	}

	/**
	 * GIVEN: an intervention present in a work order without its own ends set
	 * WHEN: atomically linking the intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkInterventionAlreadyInWorkOrder() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		workOrder._getInterventions().add(intervention);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicLink(workOrder, intervention, mechanic)
		);

		assertNull(intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
		assertTrue(workOrder.getInterventions().contains(intervention));
	}

	/**
	 * GIVEN: no intervention
	 * WHEN: atomicUnlink is called with null
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the association remains empty
	 */
	@Test
	void testAtomicUnlinkNullIntervention() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.RecordsIntervenes.atomicUnlink(null)
		);

		assertNull(intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
		assertTrue(workOrder.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: an intervention with a mechanic but no work order
	 * WHEN: atomically unlinking the intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkInterventionWithoutWorkOrder() {
		// Test-only backdoor: creates an inconsistent association end.
		intervention._setMechanic(mechanic);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicUnlink(intervention)
		);

		assertNull(intervention.getWorkOrder());
		assertSame(mechanic, intervention.getMechanic());
	}

	/**
	 * GIVEN: an intervention linked to a work order but without a mechanic
	 * WHEN: atomically unlinking the intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkInterventionWithoutMechanic() {
		// Test-only backdoors: create an inconsistent association deliberately.
		intervention._setWorkOrder(workOrder);
		workOrder._getInterventions().add(intervention);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicUnlink(intervention)
		);

		assertSame(workOrder, intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
		assertTrue(workOrder.getInterventions().contains(intervention));
	}

	/**
	 * GIVEN: an intervention whose ends are set but which is absent from the work order
	 * WHEN: atomically unlinking the intervention
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkInconsistentWorkOrder() {
		// Test-only backdoors: create an inconsistent association deliberately.
		intervention._setWorkOrder(workOrder);
		intervention._setMechanic(mechanic);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.RecordsIntervenes.atomicUnlink(intervention)
		);

		assertSame(workOrder, intervention.getWorkOrder());
		assertSame(mechanic, intervention.getMechanic());
		assertFalse(workOrder.getInterventions().contains(intervention));
	}
}
