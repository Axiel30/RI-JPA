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
 * - A substitution recovers its spare part after being persisted and reloaded
 */
class SubstitutesMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;
	private Intervention intervention;
	private VehicleType vehicleType;
	private SparePart sparePart;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		vehicleType = new VehicleType("coche", Money.of("50.0"));
		mechanic = new Mechanic(new Nif("12345678Z"));
		Associations.Classifies.link(vehicleType, vehicle);

		sparePart = new SparePart("R1001", "press tool gasket", Money.of("100.0"));

		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		intervention = DomainTestFixturesFactory.newIntervention(mechanic, workOrder, 60);
		DomainTestFixturesFactory.newSubstitution(sparePart, intervention, 1);
		workOrder.finish();
		workOrder.approve();
		
		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				sparePart);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove(
				workOrder, vehicle, invoice,
				mechanic, intervention, vehicleType,
				sparePart
			);
		factory.close();
	}

	/**
	 * GIVEN: an intervention with a substitution, both persisted
	 * WHEN: the intervention is reloaded from the database
	 * THEN: the reloaded intervention's substitutions contains that substitution
	 */
	@Test
	void testInterventionRecoversSubstitutions() {
		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId()
			);

		assertFalse( restored.getSubstitutions().isEmpty() );
		assertEquals( 1, restored.getSubstitutions().size() );
	}

	/**
	 * GIVEN: a substitution linked with an intervention, persisted
	 * WHEN: the substitution is reloaded from the database
	 * THEN: the reloaded substitution's intervention equals the given intervention
	 */
	@Test
	void testSubstitutionRecovrsItsInterventions() {
		Substitution restored = findPersistedSubstitution();

		assertEquals( intervention, restored.getIntervention() );
	}

	/**
	 * GIVEN: a substitution linked with a spare part, persisted
	 * WHEN: the substitution is reloaded from the database
	 * THEN: the reloaded substitution's spare part equals the given spare part
	 */
	@Test
	void testSubstitutionRecoversSparePart() {
		Substitution restored = findPersistedSubstitution();

		assertEquals( sparePart, restored.getSparePart() );
	}

	private Substitution findPersistedSubstitution() {
		Intervention restored = unitOfWork.findById(
				Intervention.class,
				intervention.getId()
			);
		return restored.getSubstitutions().iterator().next();
	}

}
