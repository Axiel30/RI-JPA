package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A vehicle recovers its vehicle type after being persisted and reloaded
 */
class ClassifiesMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		vehicleType = new VehicleType("type", Money.of("50.0"));


		Associations.Classifies.link(vehicleType, vehicle);

		unitOfWork.persist(vehicle, vehicleType);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, vehicleType );
		factory.close();
	}

	/**
	 * GIVEN: a vehicle type classifying a vehicle, both persisted
	 * WHEN: the vehicle is reloaded from the database
	 * THEN: the reloaded vehicle's vehicle type equals the given vehicle type
	 */
	@Test
	void testVehicleRecoversVehicleType() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( vehicleType, restored.getVehicleType() );
	}

}
