package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
 * - An intervention recovers its substitutions after being persisted and reloaded
 * - A substitution recovers its intervention after being persisted and reloaded
 * - Persisting an intervention cascades persistence to its substitutions
 * - Removing an intervention deletes its substitutions as orphans
 */
class PerformsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private VehicleType vehicleType;
	private Vehicle vehicle;
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;
	private SparePart sparePart;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over(factory);

		vehicleType = new VehicleType("car", Money.of("50.0"));
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		Associations.Classifies.link(vehicleType, vehicle);

		mechanic = new Mechanic(new Nif("12345678Z"));
		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = DomainTestFixturesFactory.newIntervention(
				mechanic,
				workOrder,
				60
			);
		sparePart = new SparePart("R1001", "gasket", Money.of("10.0"));
		DomainTestFixturesFactory.newSubstitution(sparePart, intervention, 1);

		unitOfWork.persist(
				vehicleType,
				vehicle,
				mechanic,
				sparePart,
				workOrder
			);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(workOrder, vehicle, mechanic, sparePart, vehicleType);
		factory.close();
	}

	@Test
	void testInterventionRecoversSubstitutions() {
		Intervention restored = unitOfWork.findById(
				Intervention.class,
				intervention.getId()
			);

		assertFalse(restored.getSubstitutions().isEmpty());
		assertEquals(1, restored.getSubstitutions().size());
	}

	@Test
	void testSubstitutionRecoversIntervention() {
		Substitution restored = findPersistedSubstitution();

		assertEquals(intervention, restored.getIntervention());
	}

	@Test
	void testInterventionCascadesPersistenceToSubstitution() {
		Substitution restored = findPersistedSubstitution();

		assertNotNull(restored);
	}

	@Test
	void testRemovingInterventionDeletesSubstitutionOrphan() {
		unitOfWork.execute(mapper -> {
			WorkOrder restored = mapper.find(WorkOrder.class, workOrder.getId());
			Intervention child = restored.getInterventions().iterator().next();
			restored.removeIntervention(child);
		});

		unitOfWork.execute(mapper -> {
			Number count = (Number) mapper.createQuery(
					"select count(s) from Substitution s where s.id = ?1"
				)
					.setParameter(1, sparePart.getId())
					.getSingleResult();
			assertEquals(0L, count.longValue());
		});
	}

	private Substitution findPersistedSubstitution() {
		Intervention restored = unitOfWork.findById(
				Intervention.class,
				intervention.getId()
			);
		return restored.getSubstitutions().iterator().next();
	}

}
