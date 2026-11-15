package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a vehicle type are persisted and can be reloaded correctly
 * - A vehicle type cannot be persisted if another one with the same name already exists
 */
class VehicleTypeMappingTests {

	private VehicleType vehicleType;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private static final Money FIFTY_WITH_SOMETHING = Money.of("50.1234567890");
	private static final Money SIXTY_WITH_SOMETHING = Money.of("60.1234567890");
	
	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicleType = new VehicleType("vehicleType", FIFTY_WITH_SOMETHING );
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicleType );
		factory.close();
	}

	/**
	 * GIVEN: a vehicle type persisted with all its fields set
	 * WHEN: the vehicle type is reloaded from the database
	 * THEN: all its fields (id, name and price per hour) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(vehicleType);

		VehicleType restored = unitOfWork.findById( VehicleType.class, vehicleType.getId() );

		assertEquals( vehicleType.getId(), restored.getId() );
		assertEquals( vehicleType.getName(), restored.getName() );
		assertEquals( vehicleType.getPricePerHour(), restored.getPricePerHour() );
	}

	/**
	 * GIVEN: a persisted vehicle type
	 * WHEN: a new vehicle type with the same name is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(vehicleType);
		VehicleType repeatedNameVehicleType = new VehicleType( vehicleType.getName(), SIXTY_WITH_SOMETHING );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedNameVehicleType ) 
			);
	}
}
