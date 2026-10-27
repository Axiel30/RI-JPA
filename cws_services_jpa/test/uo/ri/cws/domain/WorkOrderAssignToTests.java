package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - A work order assigned to a mechanic
 * - A work order assigned to a null mechanic throws IAE
 * - A finished work order cannot be assigned to a mechanic, throws ISE
 * - An invoiced work order cannot be assigned to a mechanic, throws ISE
 */
class WorkOrderAssignToTests {

	private Mechanic mechanic;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		vehicle = mock(Vehicle.class);
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}

	/**
	 * GIVEN: A work order in state OPENED
	 * WHEN: assigning it to a mechanic
	 * THEN: the work order is assigned to the mechanic
	 * AND: the work order state is ASSIGNED
	 */
	@Test
	void testAssignToMechanic() {
		WorkOrder wo = new WorkOrder(vehicle, "falla la junta la trocla");
		wo.assignTo(mechanic);

		assertTrue( wo.isAssigned() );
		assertSame(mechanic, wo.getMechanic());
		assertTrue(mechanic.getAssigned().contains( wo ));
		assertEquals(Money.ZERO, wo.getAmount());
	}

	/**
	 * GIVEN: A work order in state OPENED 
	 * WHEN: assigning it to a null mechanic
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testAssignToNullMechanic() {
		WorkOrder wo = new WorkOrder(vehicle, "falla la junta la trocla");
		
		assertThrows(IllegalArgumentException.class, () -> wo.assignTo(null));
		assertTrue( wo.isOpen() );
		assertNull( wo.getMechanic());
		assertTrue( mechanic.getAssigned().isEmpty() );
		assertEquals(Money.ZERO, wo.getAmount());
	}
	

	/**
	 * GIVEN: A work order in state FINISHED 
	 * WHEN: assigning it to a mechanic
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testAssignToMechanicWhenFinished() {
		Mechanic another = new Mechanic(new Nif("22222222J"), new FullName("Name-2", "Surname-2"));
 		WorkOrder wo = new WorkOrder(vehicle, "falla la junta la trocla");
		wo.assignTo(mechanic);
		wo.finish();

		assertThrows(IllegalStateException.class, () -> wo.assignTo( another ));
		assertEquals(Money.ZERO, wo.getAmount());
	}

	/**
	 * GIVEN: A work order in state INVOICED
	 * WHEN: assigning it to a mechanic
	 * THEN: an IllegalStateException is thrown
	 */
	@Test
	void testAssignToMechanicWhenInvoiced() {
		WorkOrder wo = new WorkOrder(vehicle, "falla la junta la trocla");
		wo.assignTo(mechanic);
		wo.finish();
		wo.approve();
		Invoice invoice = new Invoice(1000L);
		invoice.addWorkOrder(wo);
		Mechanic another = new Mechanic(new Nif("22222222J"), new FullName("Name-2", "Surname-2"));

		assertThrows(IllegalStateException.class, () -> wo.assignTo(another));
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
}
