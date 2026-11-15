package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a vehicle are persisted and can be reloaded correctly
 * - A vehicle cannot be persisted if another one with the same license plate already exists
 */
class VehicleMappingTests {

	private Vehicle vehicle;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle );
		factory.close();
	}

	/**
	 * GIVEN: a vehicle persisted with all its fields set
	 * WHEN: the vehicle is reloaded from the database
	 * THEN: all its fields (id, license plate, make and model) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(vehicle);

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicle.getId(), restored.getId() );
		assertEquals( vehicle.getLicensePlate(), restored.getLicensePlate() );
		assertEquals( vehicle.getMake(), restored.getMake() );
		assertEquals( vehicle.getModel(), restored.getModel() );
	}

	/**
	 * GIVEN: a persisted vehicle
	 * WHEN: a new vehicle with the same license plate is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(vehicle);
		Vehicle repeatedPlateVehicle = new Vehicle( vehicle.getLicensePlate() );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedPlateVehicle )
			);
	}
}