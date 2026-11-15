package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.DomainTestFixturesFactory;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A work order recovers its interventions after being persisted and reloaded
 * - An intervention recovers its work order after being persisted and reloaded
 * - Persisting a work order cascades persistence to its interventions
 * - Removing an intervention from a work order deletes the orphan
 */
class RecordsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private VehicleType vehicleType;
	private Vehicle vehicle;
	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Intervention intervention;

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

		unitOfWork.persist(vehicleType, vehicle, mechanic, workOrder);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(workOrder, vehicle, mechanic, vehicleType);
		factory.close();
	}

	@Test
	void testWorkOrderRecoversInterventions() {
		WorkOrder restored = unitOfWork.findById(WorkOrder.class, workOrder.getId());

		assertFalse(restored.getInterventions().isEmpty());
		assertEquals(1, restored.getInterventions().size());
	}

	@Test
	void testInterventionRecoversWorkOrder() {
		Intervention restored = unitOfWork.findById(
				Intervention.class,
				intervention.getId()
			);

		assertEquals(workOrder, restored.getWorkOrder());
	}

	@Test
	void testWorkOrderCascadesPersistenceToIntervention() {
		Intervention restored = unitOfWork.findById(
				Intervention.class,
				intervention.getId()
			);

		assertNotNull(restored);
	}

	@Test
	void testRemovingInterventionDeletesOrphan() {
		unitOfWork.execute(mapper -> {
			WorkOrder restored = mapper.find(WorkOrder.class, workOrder.getId());
			Intervention child = restored.getInterventions().iterator().next();
			restored.removeIntervention(child);
		});

		assertNull(unitOfWork.findById(Intervention.class, intervention.getId()));
	}

}
