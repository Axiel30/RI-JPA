package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.DomainTestFixturesFactory;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A work order recovers its interventions after being persisted and reloaded
 * - An intervention recovers its work order after being persisted and reloaded
 * - An intervention recovers its mechanic after being persisted and reloaded
 */
class IntervenesMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		vehicleType = new VehicleType("coche", Money.of("50.0"));
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = DomainTestFixturesFactory.newIntervention(mechanic, workOrder, 60);
		workOrder.finish();
		workOrder.approve();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType
			);
		factory.close();
	}

	/**
	 * GIVEN: a work order with an intervention, both persisted
	 * WHEN: the work order is reloaded from the database
	 * THEN: the reloaded work order's interventions contains that intervention
	 */
	@Test
	void testWorkOrderRecoversInterventions() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertFalse( restored.getInterventions().isEmpty() );
		assertEquals( 1, restored.getInterventions().size() );
	}

	/**
	 * GIVEN: an intervention linked with a work order, both persisted
	 * WHEN: the intervention is reloaded from the database
	 * THEN: the reloaded intervention's work order equals the given work order
	 */
	@Test
	void testInterventionRecoversWorkOrder() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( workOrder, restored.getWorkOrder() );
	}

	/**
	 * GIVEN: an intervention linked with a mechanic, both persisted
	 * WHEN: the intervention is reloaded from the database
	 * THEN: the reloaded intervention's mechanic equals the given mechanic
	 */
	@Test
	void testInterventionRecoversMechanic() {

		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}
