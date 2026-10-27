package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - An intervention can be removed from an ASSIGNED work order
 * - An intervention can be removed from a FINISHED work order
 * - Removing an intervention asks it to remove its substitutions
 * - Removing does not recompute the work order amount before approval
 * - A null intervention is rejected
 * - An intervention belonging to another work order is rejected
 */
class WorkOrderRemoveInterventionTests {

	private WorkOrder workOrder;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		Vehicle vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
	}

	/**
	 * GIVEN: an assigned work order with an intervention
	 * WHEN: removing the intervention
	 * THEN: the intervention is removed and unlinked
	 * AND: the intervention is asked to remove its substitutions
	 */
	@Test
	void testRemoveFromAssignedWorkOrder() {
		Intervention intervention = linkedIntervention();

		workOrder.removeIntervention(intervention);

		verify(intervention).removeSubstitutions();
		assertTrue(workOrder.getInterventions().isEmpty());
		assertNull(intervention.getWorkOrder());
		assertNull(intervention.getMechanic());
		assertEquals(Money.ZERO, workOrder.getAmount());
	}

	/**
	 * GIVEN: a finished work order with an intervention
	 * WHEN: the foreman removes the intervention
	 * THEN: it is removed while the work order remains FINISHED
	 * AND: its amount remains zero until approval
	 */
	@Test
	void testRemoveFromFinishedWorkOrder() {
		Intervention intervention = linkedIntervention();
		workOrder.finish();

		workOrder.removeIntervention(intervention);

		assertTrue(workOrder.getInterventions().isEmpty());
		assertTrue(workOrder.isFinished());
		assertEquals(Money.ZERO, workOrder.getAmount());
	}

	/**
	 * GIVEN: an assigned work order
	 * WHEN: trying to remove a null intervention
	 * THEN: an IllegalArgumentException is thrown and nothing changes
	 */
	@Test
	void testNullInterventionIsRejected() {
		Intervention intervention = linkedIntervention();

		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.removeIntervention(null)
			);

		assertTrue(workOrder.getInterventions().contains(intervention));
	}

	/**
	 * GIVEN: two assigned work orders and an intervention belonging to one of them
	 * WHEN: trying to remove it from the other work order
	 * THEN: an IllegalStateException is thrown and both aggregates are unchanged
	 */
	@Test
	void testInterventionFromAnotherWorkOrderIsRejected() {
		Intervention intervention = linkedIntervention();
		Vehicle anotherVehicle = new Vehicle(new LicensePlate("5678 JKL"), "seat", "leon");
		WorkOrder anotherWorkOrder = new WorkOrder(anotherVehicle, "another failure");
		anotherWorkOrder.assignTo(mechanic);

		assertThrows(
				IllegalStateException.class,
				() -> anotherWorkOrder.removeIntervention(intervention)
			);

		assertTrue(workOrder.getInterventions().contains(intervention));
		assertTrue(anotherWorkOrder.getInterventions().isEmpty());
	}

	private Intervention linkedIntervention() {
		workOrder.assignTo(mechanic);

		Intervention intervention = DomainTestMocksFactory.oneIntervention();
		Associations.RecordsIntervenes.atomicLink(
				workOrder,
				intervention,
				mechanic
			);

		return intervention;
	}
}
