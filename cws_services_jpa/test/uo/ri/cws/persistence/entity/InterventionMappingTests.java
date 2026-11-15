package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.DomainTestFixturesFactory;
import uo.ri.cws.domain.Intervention;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of an intervention are persisted and can be reloaded correctly
 * - An intervention cannot be persisted if another one exists for the same mechanic, work order and date
 */
class InterventionMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Intervention intervention;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");

		LocalDateTime now = LocalDateTime.now();
		workOrder = new WorkOrder(vehicle, now, "description");
		workOrder.assignTo(mechanic);
		workOrder.finish();

		intervention = DomainTestFixturesFactory.newIntervention(mechanic, workOrder, 60);

		unitOfWork.persist(vehicle, intervention, workOrder, mechanic);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, workOrder, mechanic, intervention );
		factory.close();
	}

	/**
	 * GIVEN: an intervention persisted with a mechanic and a work order
	 * WHEN: the intervention is reloaded from the database
	 * THEN: all its fields (id, mechanic, work order, date, minutes and substitutions) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		Intervention restored = unitOfWork.findById( Intervention.class,
				intervention.getId() );

		assertEquals( intervention.getId(), restored.getId() );
		assertEquals( intervention.getMechanic(), restored.getMechanic() );
		assertEquals( intervention.getWorkOrder(), restored.getWorkOrder() );
		assertEquals( 
				intervention.getDate().truncatedTo(ChronoUnit.MILLIS),
				restored.getDate().truncatedTo(ChronoUnit.MILLIS) 
			);
		assertEquals( intervention.getMinutes(), restored.getMinutes() );
		assertEquals( intervention.getSubstitutions(), restored.getSubstitutions() );
	}

	/**
	 * GIVEN: a persisted intervention for a mechanic, a work order and a date
	 * WHEN: a new intervention is created for the same mechanic, work order and date, and persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		Intervention repeatedIntervention = DomainTestFixturesFactory.newIntervention(
				intervention.getMechanic(),
				intervention.getWorkOrder(),
				intervention.getDate(),
				100
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedIntervention )
			);
	}
}
