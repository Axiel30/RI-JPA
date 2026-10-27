package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * To better understand these tests, please review the WorkOrder state diagram
 * in the "project scope statement" document.
 *
 * Scenarios:
 * - A FINISHED work order can be marked as approved
 * - Approving computes the amount from every current intervention
 * - The computed amount is rounded to cents only after adding all interventions
 */
class WorkOrderApproveTests {

	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
	}

	/**
	 * GIVEN: A finished work order with one intervention
	 * WHEN: marking it as approved
	 * THEN: the work order state is APPROVED
	 * AND: its amount is computed from the intervention
	 */
	@Test
	void testMarkAsApproved() {
		WorkOrder workOrder = finishedWorkOrder(Money.of("250.00"));
		assertEquals(Money.ZERO, workOrder.getAmount());

		workOrder.approve();

		assertTrue(workOrder.isApproved());
		assertEquals(Money.of("250.00"), workOrder.getAmount());
	}

	/**
	 * GIVEN: A finished work order with several interventions
	 * WHEN: marking it as approved
	 * THEN: its amount is the sum of all intervention amounts
	 */
	@Test
	void testApproveSumsAllCurrentInterventions() {
		WorkOrder workOrder = finishedWorkOrder(
				Money.of("250.00"),
				Money.of("75.50")
			);

		workOrder.approve();

		assertEquals(Money.of("325.50"), workOrder.getAmount());
	}

	/**
	 * GIVEN: A finished work order with high-resolution intermediate amounts
	 * WHEN: marking it as approved
	 * THEN: the sum is rounded to cents using HALF_EVEN
	 */
	@Test
	void testApproveRoundsTheFinalAmountToCents() {
		WorkOrder workOrder = finishedWorkOrder(
				Money.of("10.005"),
				Money.of("0.010")
			);

		workOrder.approve();

		assertEquals(Money.of("10.02"), workOrder.getAmount());
	}

	private WorkOrder finishedWorkOrder(Money... interventionAmounts) {
		WorkOrder workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);

		for (Money amount : interventionAmounts) {
			Intervention intervention = DomainTestMocksFactory.oneInterventionWithAmount(amount);
			// Test-only backdoor: approval is isolated from addIntervention().
			workOrder._getInterventions().add(intervention);
		}

		workOrder.finish();
		return workOrder;
	}
}
