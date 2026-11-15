package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.DomainTestFixturesFactory;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.Substitution;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields and the artificial identity of a substitution are persisted
 * - A substitution cannot be repeated for one intervention and spare part
 */
class SubstitutionMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;
	private VehicleType vehicleType;
	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;
	private SparePart sparePart;
	private Substitution substitution;

	private static final Money ORIGINAL_PRICE = Money.of("100.1234567890");
	private static final Money STORED_PRICE = Money.of("100.12");
	private static final Money FIFTY = Money.of("50.0");

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over(factory);

		vehicleType = new VehicleType("vehicle-type", FIFTY);
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, LocalDateTime.now(), "description");
		mechanic = new Mechanic(new Nif("12345678Z"));
		workOrder.assignTo(mechanic);
		intervention = DomainTestFixturesFactory.newIntervention(
				mechanic,
				workOrder,
				60
			);
		sparePart = new SparePart(
				"spare-code",
				"spare description",
				ORIGINAL_PRICE
			);
		substitution = DomainTestFixturesFactory.newSubstitution(
				sparePart,
				intervention,
				1
			);
		workOrder.finish();

		unitOfWork.persist(
				vehicleType,
				mechanic,
				vehicle,
				workOrder,
				sparePart
			);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(workOrder, vehicle, mechanic, vehicleType, sparePart);
		factory.close();
	}

	@Test
	void testAllFieldsPersisted() {
		Substitution restored = unitOfWork.findById(
				Substitution.class,
				substitution.getId()
			);

		assertEquals(substitution.getId(), restored.getId());
		assertEquals(1, restored.getQuantity());
		assertEquals(STORED_PRICE, restored.getPrice());
		assertEquals(intervention, restored.getIntervention());
		assertEquals(sparePart, restored.getSparePart());
		assertEquals(STORED_PRICE, restored.getTotalAmount());
	}

	@Test
	void testRepeated() {
		Substitution newSubstitution = DomainTestFixturesFactory.newSubstitution(sparePart, intervention, 2);
		assertThrows(PersistenceException.class, () -> unitOfWork.persist(newSubstitution));
	}
	
}
