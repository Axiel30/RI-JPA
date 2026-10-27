package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
 * - An ASSIGNED work order can be marked as finished
 */
class WorkOrderFinishTests {

	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}

	/**
	 * GIVEN: A work order in state ASSIGNED
	 * WHEN: marking it as finished
	 * THEN: the work order state is FINISHED
	 * AND: the work order is no longer assigned to the mechanic
	 */
	@Test
	void testMarkAsFinished() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);

		wo.finish();
		assertTrue( wo.isFinished() );
		assertEquals(Money.ZERO, wo.getAmount());

		assertTrue( wo.getMechanic() == null );
		assertFalse( mechanic.getAssigned().contains(wo) );
	}
	
}
