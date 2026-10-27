package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Assigning a mechanic to a work order links both sides of the association
 * - Unlinking the assign association removes the link on both sides
 * - The collection returned by getAssigned() is a safe copy
 * - Linking rejects a null mechanic
 * - Linking rejects a null work order
 * - Linking rejects a work order that already has a mechanic
 * - Linking rejects a work order already present in the mechanic's assigned work orders
 * - Unlinking rejects a null work order
 * - Unlinking rejects a work order without a mechanic
 * - Unlinking rejects an inconsistent association
 */
class AssociationsAssignsTests {
	private Mechanic mechanic;
	private WorkOrder workOrder;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneWorkOrder();
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}

	/**
	 * GIVEN: a mechanic and a work order linked through Associations.Assigns
	 * WHEN: querying both objects
	 * THEN: the work order is in the mechanic's assigned set
	 * AND: the mechanic is the work order's mechanic
	 */
	@Test
	void testLinkOnAssign() {
		Associations.Assigns.link(mechanic, workOrder);

		assertTrue(mechanic.getAssigned().contains(workOrder));
		assertSame(mechanic, workOrder.getMechanic());
	}

	/**
	 * GIVEN: a mechanic and a work order linked
	 * WHEN: Associations.Assigns.unlink is called
	 * THEN: the work order is removed from the mechanic's assigned set
	 * AND: the work order's mechanic is null
	 */
	@Test
	void testUnlinkOnAssign() {
		Associations.Assigns.link(mechanic, workOrder);

		Associations.Assigns.unlink(workOrder);

		assertTrue(mechanic.getAssigned().isEmpty());
		assertNull(workOrder.getMechanic());
	}

	/**
	 * GIVEN: a mechanic with an assigned work order
	 * WHEN: the returned assigned set is modified
	 * THEN: the original association is not affected (it must be a copy)
	 */
	@Test
	void testSafeReturn() {
		Associations.Assigns.link(mechanic, workOrder);

		Set<WorkOrder> assigned = mechanic.getAssigned();
		assigned.remove(workOrder);

		assertTrue(assigned.isEmpty());

		assertEquals(1, mechanic.getAssigned().size(), "It must be a copy of the collection");
	}

	/**
	 * GIVEN: an assigned work order
	 * WHEN: linking it with a null mechanic
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and mechanic remain unlinked
	 */
	@Test
	void testLinkNullMechanic() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Assigns.link(null, workOrder)
		);

		assertNull(workOrder.getMechanic());
		assertTrue(mechanic.getAssigned().isEmpty());
	}

	/**
	 * GIVEN: a mechanic
	 * WHEN: linking it with a null work order
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the assigned work orders remain unchanged
	 */
	@Test
	void testLinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Assigns.link(mechanic, null)
		);

		assertTrue(mechanic.getAssigned().isEmpty());
	}

	/**
	 * GIVEN: a work order already assigned to a mechanic
	 * WHEN: linking it to another mechanic
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkWorkOrderWithMechanic() {
		Mechanic another = new Mechanic(
			new Nif("87654321X"),
			new FullName("Another", "Mechanic")
		);
		Associations.Assigns.link(mechanic, workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Assigns.link(another, workOrder)
		);

		assertSame(mechanic, workOrder.getMechanic());
		assertTrue(mechanic.getAssigned().contains(workOrder));
		assertTrue(another.getAssigned().isEmpty());
	}

	/**
	 * GIVEN: a work order present in a mechanic but without its mechanic end
	 * WHEN: linking both objects
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkWorkOrderAlreadyInMechanic() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		mechanic._getAssigned().add(workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Assigns.link(mechanic, workOrder)
		);

		assertNull(workOrder.getMechanic());
		assertTrue(mechanic.getAssigned().contains(workOrder));
	}

	/**
	 * GIVEN: no work order
	 * WHEN: Associations.Assigns.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and mechanic remain unlinked
	 */
	@Test
	void testUnlinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Assigns.unlink(null)
		);

		assertNull(workOrder.getMechanic());
		assertTrue(mechanic.getAssigned().isEmpty());
	}

	/**
	 * GIVEN: a work order without a mechanic
	 * WHEN: Associations.Assigns.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the work order remains unassigned
	 */
	@Test
	void testUnlinkWorkOrderWithoutMechanic() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Assigns.unlink(workOrder)
		);

		assertNull(workOrder.getMechanic());
		assertTrue(mechanic.getAssigned().isEmpty());
	}

	/**
	 * GIVEN: a work order whose mechanic does not contain it
	 * WHEN: Associations.Assigns.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentAssociation() {
		// Test-only backdoor: creates an inconsistent association end.
		workOrder._setMechanic(mechanic);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Assigns.unlink(workOrder)
		);

		assertSame(mechanic, workOrder.getMechanic());
		assertFalse(mechanic.getAssigned().contains(workOrder));
	}
}
