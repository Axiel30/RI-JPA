package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
 * 
 * Scenarios:
 * - An ASSIGNED work order can be unassigned from a mechanic
 */
class WorkOrderUnassignTests {
	
	private Vehicle vehicle;
	private Mechanic mechanic;
	private WorkOrder workorder;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "make", "model");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name-1", "Surname-1"));
		
		workorder = new WorkOrder(vehicle, "falla la junta la trocla");
	}
	
	/**
	 * GIVEN: A work order in state ASSIGNED 
	 * WHEN: unassigning it 
	 * THEN: the work order is unassigned from the mechanic 
	 * AND: the work order state is OPEN
	 */
	@Test
	void testUnassignFromMechanic() {
		workorder.assignTo(mechanic);

		workorder.unassign();

		assertTrue( workorder.isOpen() );
		assertNull( workorder.getMechanic() );
		assertFalse( mechanic.getAssigned().contains(workorder) );
		assertEquals(Money.ZERO, workorder.getAmount());
	}

}
