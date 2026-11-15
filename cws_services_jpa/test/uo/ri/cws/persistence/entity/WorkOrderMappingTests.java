package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a work order are persisted and can be reloaded correctly
 * - A work order cannot be persisted if another one exists for the same vehicle and date
 */
class WorkOrderMappingTests {

	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private UnitOfWork unitOfWork;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");

		unitOfWork.persist(vehicle, workOrder);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * GIVEN: a work order persisted for a vehicle
	 * WHEN: the work order is reloaded from the database
	 * THEN: all its fields (id, vehicle, date, description, amount and state) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( workOrder.getId(), restored.getId() );
		assertEquals( workOrder.getVehicle(), restored.getVehicle() ); // same id
		assertEquals( workOrder.getDate().truncatedTo(ChronoUnit.MILLIS), 
				restored.getDate().truncatedTo(ChronoUnit.MILLIS));
		assertEquals( workOrder.getDescription(), restored.getDescription() );
		assertEquals( workOrder.getAmount(), restored.getAmount() );
		assertEquals( workOrder.getState(), restored.getState() );
	}

	/**
	 * GIVEN: a persisted work order for a vehicle at a given date
	 * WHEN: a new work order is created for the same vehicle and date, and persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		WorkOrder repeatedWorkOrder = new WorkOrder(
				workOrder.getVehicle(),
				workOrder.getDate(),
				"another description"
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedWorkOrder ) 
			);
	}
}