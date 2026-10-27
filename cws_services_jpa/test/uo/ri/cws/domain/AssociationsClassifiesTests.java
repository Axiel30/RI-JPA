package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;

/**
 * Scenarios:
 * - Linking a vehicle type and a vehicle updates the vehicle association end
 * - Unlinking a vehicle type and a vehicle removes the link from the vehicle
 * - Linking rejects a null vehicle type
 * - Linking rejects a null vehicle
 * - Linking rejects a vehicle that already has a vehicle type
 * - Unlinking rejects a null vehicle
 * - Unlinking rejects a vehicle without a vehicle type
 */
class AssociationsClassifiesTests {
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		vehicleType = new VehicleType("car", Money.of("50.0"));
	}

	/**
	 * GIVEN: a vehicle type and a vehicle linked through Associations.Classifies
	 * WHEN: querying the vehicle
	 * THEN: the vehicle type is the vehicle's vehicle type
	 */
	@Test
	void testLinkOnClassify() {
		Associations.Classifies.link(vehicleType, vehicle);

		assertSame( vehicleType, vehicle.getVehicleType() );
	}

	/**
	 * GIVEN: a vehicle type and a vehicle linked
	 * WHEN: Associations.Classifies.unlink is called
	 * THEN: the vehicle's vehicle type is null
	 */
	@Test
	void testUnlinkOnClassify() {
		Associations.Classifies.link(vehicleType, vehicle);

		Associations.Classifies.unlink(vehicle);

		assertNull( vehicle.getVehicleType() );
	}

	/**
	 * GIVEN: a linked vehicle
	 * WHEN: linking it with a null vehicle type
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the vehicle remains unlinked
	 */
	@Test
	void testLinkNullVehicleType() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Classifies.link(null, vehicle)
		);

		assertNull( vehicle.getVehicleType() );
	}

	/**
	 * GIVEN: a vehicle type
	 * WHEN: linking it with a null vehicle
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testLinkNullVehicle() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Classifies.link(vehicleType, null)
		);
	}

	/**
	 * GIVEN: a vehicle already classified by a vehicle type
	 * WHEN: linking it to another vehicle type
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkVehicleWithVehicleType() {
		VehicleType another = new VehicleType("motorcycle", Money.of("30.0"));
		Associations.Classifies.link(vehicleType, vehicle);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Classifies.link(another, vehicle)
		);

		assertSame( vehicleType, vehicle.getVehicleType() );
	}

	/**
	 * GIVEN: no vehicle
	 * WHEN: Associations.Classifies.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the vehicle remains unlinked
	 */
	@Test
	void testUnlinkNullVehicle() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Classifies.unlink(null)
		);

		assertNull( vehicle.getVehicleType() );
	}

	/**
	 * GIVEN: a vehicle without a vehicle type
	 * WHEN: Associations.Classifies.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the vehicle remains unclassified
	 */
	@Test
	void testUnlinkVehicleWithoutVehicleType() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Classifies.unlink(vehicle)
		);

		assertNull( vehicle.getVehicleType() );
	}
}
