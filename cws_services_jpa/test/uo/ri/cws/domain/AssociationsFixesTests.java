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

import uo.ri.cws.domain.types.LicensePlate;

/**
 * Scenarios:
 * - Creating a work order for a vehicle links both sides of the association
 * - Unlinking a work order from a vehicle removes the link on both sides
 * - The collection returned by getWorkOrders() is a safe copy
 * - Linking rejects a null vehicle
 * - Linking rejects a null work order
 * - Linking rejects a work order that already has a vehicle
 * - Linking rejects a work order already present in the vehicle's work orders
 * - Unlinking rejects a null work order
 * - Unlinking rejects a work order without a vehicle
 * - Unlinking rejects an inconsistent association
 */
class AssociationsFixesTests {
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		workOrder = DomainTestMocksFactory.oneWorkOrder();
	}
	
	/**
	 * GIVEN: a vehicle and a work order created for it (the constructor links them)
	 * WHEN: querying both objects
	 * THEN: the work order is in the vehicle's work orders
	 * AND: the vehicle is the work order's vehicle
	 */
	@Test
	void testLinkOnOrder() {
		Associations.Fixes.link(vehicle, workOrder);

		assertTrue( vehicle.getWorkOrders().contains( workOrder ));
		assertSame( vehicle, workOrder.getVehicle() );
	}

	/**
	 * GIVEN: a vehicle and a work order linked
	 * WHEN: Associations.Fixes.unlink is called
	 * THEN: the work order is removed from the vehicle's work orders
	 * AND: the work order's vehicle is null
	 */
	@Test
	void testUnlinkOnOrder() {
		Associations.Fixes.link(vehicle, workOrder);

		Associations.Fixes.unlink(workOrder);
		
		assertFalse( vehicle.getWorkOrders().contains( workOrder ));
		assertNull( workOrder.getVehicle() );
	}

	/**
	 * GIVEN: a work order without a vehicle
	 * WHEN: Associations.Fixes.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the work order remains unlinked
	 */
	@Test
	void testUnlinkWorkOrderWithoutVehicle() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Fixes.unlink(workOrder)
		);
		
		assertFalse( vehicle.getWorkOrders().contains( workOrder ));
		assertNull( workOrder.getVehicle() );
	}

	/**
	 * GIVEN: a vehicle with a linked work order
	 * WHEN: the returned work orders set is modified
	 * THEN: the original association is not affected (it must be a copy)
	 */
	@Test
	void testSafeReturn() {
		Associations.Fixes.link(vehicle, workOrder);

		Set<WorkOrder> workOrders = vehicle.getWorkOrders();
		workOrders.remove( workOrder );

		assertTrue( workOrders.isEmpty() );
		assertEquals( 1, vehicle.getWorkOrders().size() );
	}

	/**
	 * GIVEN: a linked work order
	 * WHEN: linking it with a null vehicle
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and vehicle remain unlinked
	 */
	@Test
	void testLinkNullVehicle() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Fixes.link(null, workOrder)
		);

		assertNull(workOrder.getVehicle());
		assertTrue(vehicle.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a vehicle
	 * WHEN: linking it with a null work order
	 * THEN: an IllegalArgumentException is thrown
	 * AND: its work orders remain unchanged
	 */
	@Test
	void testLinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Fixes.link(vehicle, null)
		);

		assertTrue(vehicle.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order already linked to a vehicle
	 * WHEN: linking it to another vehicle
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkWorkOrderWithVehicle() {
		Vehicle another = new Vehicle(new LicensePlate("5678 KLM"), "seat", "leon");
		Associations.Fixes.link(vehicle, workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Fixes.link(another, workOrder)
		);

		assertSame(vehicle, workOrder.getVehicle());
		assertTrue(vehicle.getWorkOrders().contains(workOrder));
		assertTrue(another.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order present in a vehicle but without its vehicle end
	 * WHEN: linking both objects
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkWorkOrderAlreadyInVehicle() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		vehicle._getWorkOrders().add(workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Fixes.link(vehicle, workOrder)
		);

		assertNull(workOrder.getVehicle());
		assertTrue(vehicle.getWorkOrders().contains(workOrder));
	}

	/**
	 * GIVEN: no work order
	 * WHEN: Associations.Fixes.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and vehicle remain unlinked
	 */
	@Test
	void testUnlinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Fixes.unlink(null)
		);

		assertNull(workOrder.getVehicle());
		assertTrue(vehicle.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order whose vehicle does not contain it
	 * WHEN: Associations.Fixes.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentAssociation() {
		// Test-only backdoor: creates an inconsistent association end.
		workOrder._setVehicle(vehicle);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Fixes.unlink(workOrder)
		);

		assertSame(vehicle, workOrder.getVehicle());
		assertFalse(vehicle.getWorkOrders().contains(workOrder));
	}
}
