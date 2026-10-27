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
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - An APPROVED work order can be marked back to finished
 */
class WorkOrderUnapproveTests {

	private static final Money HOURLY_RATE = Money.of("50.0");

	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}
	
	/**
	 * GIVEN: A work order in state APPROVED 
	 * WHEN: marking it back to finished
	 * THEN: the work order state is FINISHED
	 */
	@Test
	void testMarkBackToFinished() {
		WorkOrder wo = oneApprovedWorkOrder();

		wo.unapprove();

		assertTrue(wo.isFinished());
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
	private WorkOrder oneApprovedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		Intervention intervention = DomainTestMocksFactory.oneInterventionWithAmount(HOURLY_RATE);
		// Test-only backdoor: unapproval is isolated from addIntervention().
		wo._getInterventions().add(intervention);
		wo.finish();
		wo.approve();
		return wo;
	}

}
