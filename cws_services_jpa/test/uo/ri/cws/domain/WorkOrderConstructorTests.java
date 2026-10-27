package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * 
 * Scenarios:
 * 	- A work order for a Vehicle
 * 	- A work order for a Vehicle in a date
 *  - A work order for a Vehicle with a description
 * 	- A work order for a Vehicle, in a date with a description
 *  - A work order with null vehicle throws IAE
 *  - A work order with null date throws IAE
 *  - A work order with blank description throws IAE
 */
class WorkOrderConstructorTests {
	
	private Vehicle vehicle;
	
	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "make", "model");
	}

	/**
	 * GIVEN: a vehicle
	 * WHEN: we create a work order with that vehicle
	 * THEN: the work order is created with that vehicle, the current date
	 * 
	 * Note:
	 *   The date of a work order must be stored truncated to milliseconds
	 *   in the constructor with:
	 * 		this.date = date.truncatedTo(ChronoUnit.MILLIS);
	 *   To avoid problems with databases loss of precision
	 */
	@Test
	void testWorkOrderForVehicle() {
		WorkOrder wo = new WorkOrder(vehicle);

		LocalDateTime date = wo.getDate();
		assertEquals(date.truncatedTo(ChronoUnit.MILLIS), date);
		
		assertEquals(vehicle, wo.getVehicle());
		assertTrue( vehicle.getWorkOrders().contains( wo ) );
		
		assertTrue( wo.isOpen() );
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
	/**
	 * GIVEN: a vehicle and a date 
	 * WHEN: we create a work order with that vehicle and date 
	 * THEN: the work order is created with that vehicle and date
	 */
	@Test
	void testWorkOrderForVehicleAndDate() {
		LocalDateTime now = LocalDateTime.now();
		WorkOrder wo = new WorkOrder(vehicle, now);

		assertEquals(now.truncatedTo(ChronoUnit.MILLIS), wo.getDate());
		assertEquals(vehicle, wo.getVehicle());
		assertTrue( vehicle.getWorkOrders().contains( wo ) );
		
		assertTrue( wo.isOpen() );
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
	/**
	 * GIVEN: a vehicle and a description 
	 * WHEN: we create a work order with that vehicle and description 
	 * THEN: the work order is created with that vehicle and description
	 */
	@Test
	void testWorkOrderForVehicleAndDescription() {
		String desc = "description";
		WorkOrder wo = new WorkOrder(vehicle, desc);

		LocalDateTime date = wo.getDate();
		assertEquals(date.truncatedTo(ChronoUnit.MILLIS), date);
		assertEquals(vehicle, wo.getVehicle());
		assertEquals(desc, wo.getDescription());
		assertTrue(vehicle.getWorkOrders().contains(wo));
		
		assertTrue(wo.isOpen());
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
	/**
	 * GIVEN: a vehicle, a date and a description 
	 * WHEN: we create a work order with that vehicle, date and description 
	 * THEN: the work order is created with that vehicle, date and description
	 */
	@Test
	void testWorkOrderForVehicleAndDateAndDescription() {
		LocalDateTime now = LocalDateTime.now();
		String desc = "description";
		WorkOrder wo = new WorkOrder(vehicle, now, desc);

		assertEquals(now.truncatedTo(ChronoUnit.MILLIS), wo.getDate());
		assertEquals(vehicle, wo.getVehicle());
		assertEquals(desc, wo.getDescription());
		assertTrue(vehicle.getWorkOrders().contains(wo));
		
		assertTrue(wo.isOpen());
		assertEquals(Money.ZERO, wo.getAmount());
	}
	
	/**
	 * GIVEN: a null vehicle
	 * WHEN: we create a work order with that vehicle
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testWorkOrderForNullVehicle() {
		LocalDateTime now = LocalDateTime.now();

		assertThrows(IllegalArgumentException.class, () -> new WorkOrder(null));
		assertThrows(IllegalArgumentException.class, () -> new WorkOrder(null, now));
		assertThrows(IllegalArgumentException.class, () -> new WorkOrder(null, "desc"));
		assertThrows(IllegalArgumentException.class, () -> new WorkOrder(null, now, "desc"));
	}
	
}
