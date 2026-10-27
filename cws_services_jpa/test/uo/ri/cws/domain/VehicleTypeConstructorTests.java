package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;

/**
 * Scenarios:
 * - Vehicle type name cannot be null, empty or blank
 * - Vehicle type price per hour cannot be null
 * - Vehicle type price per hour cannot be negative
 * - Vehicle type price per hour can be zero
 * - Vehicle type price per hour is rounded to cents using HALF_EVEN
 */
class VehicleTypeConstructorTests {

	/**
	 * GIVEN: a null, empty or blank name
	 * WHEN: we create a vehicle type with that name
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNameCannotBeNullEmptyOrBlank() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new VehicleType(null, Money.ZERO)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new VehicleType("", Money.ZERO)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new VehicleType(" ", Money.ZERO)
			);
	}

	/**
	 * GIVEN: a null price per hour
	 * WHEN: we create a vehicle type with that price
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testPricePerHourCannotBeNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new VehicleType("car", null)
			);
	}

	/**
	 * GIVEN: a negative price per hour
	 * WHEN: we create a vehicle type with that price
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testPricePerHourCannotBeNegative() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new VehicleType("car", Money.of("-0.01"))
			);
	}

	/**
	 * GIVEN: a zero price per hour
	 * WHEN: we create a vehicle type with that price
	 * THEN: the vehicle type is created with a zero price per hour
	 */
	@Test
	void testPricePerHourCanBeZero() {
		VehicleType vehicleType = new VehicleType("car", Money.ZERO);

		assertEquals(Money.ZERO, vehicleType.getPricePerHour());
	}

	/**
	 * GIVEN: a price per hour whose resolution is higher than cents
	 * WHEN: we create a vehicle type with that price
	 * THEN: its price per hour is rounded to cents using HALF_EVEN
	 */
	@Test
	void testPricePerHourIsRoundedToCentsUsingHalfEven() {
		VehicleType vehicleType = new VehicleType("car", Money.of("10.005"));

		assertEquals(Money.of("10.00").amount(), vehicleType.getPricePerHour().amount());
	}
}
