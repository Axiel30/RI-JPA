package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A vehicle recovers its work orders after being persisted and reloaded
 * - A work order recovers its vehicle after being persisted and reloaded
 */
class FixesMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		workOrder = new WorkOrder(vehicle);

		unitOfWork.persist(workOrder, vehicle);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle );
		factory.close();
	}

	/**
	 * GIVEN: a vehicle with a work order created for it, both persisted
	 * WHEN: the vehicle is reloaded from the database
	 * THEN: the reloaded vehicle's work orders contains that work order
	 */
	@Test
	void testVehicleRecoversWorkOrders() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertFalse( restored.getWorkOrders().isEmpty() );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * GIVEN: a vehicle with a work order created for it, both persisted
	 * WHEN: the work order is reloaded from the database
	 * THEN: the reloaded work order's vehicle equals the given vehicle
	 */
	@Test
	void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( vehicle, restored.getVehicle() );
	}

}