package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A mechanic recovers its assigned work orders after being persisted and reloaded
 * - A work order recovers its mechanic after being persisted and reloaded
 */
class AssignsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Mechanic mechanic;
	private Vehicle vehicle;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));

		workOrder = new WorkOrder( vehicle );
		workOrder.assignTo(mechanic);

		unitOfWork.persist(workOrder, mechanic, vehicle);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, mechanic, vehicle );
		factory.close();
	}

	/**
	 * GIVEN: a mechanic assigned to a work order, both persisted
	 * WHEN: the mechanic is reloaded from the database
	 * THEN: the reloaded mechanic's assigned work orders contains that work order
	 */
	@Test
	void testMechanicRecoversWorkOrders() {

		Mechanic restored = unitOfWork.findById( Mechanic.class, mechanic.getId() );

		assertFalse( restored.getAssigned().isEmpty() );
		assertEquals( 1, restored.getAssigned().size() );
	}

	/**
	 * GIVEN: a mechanic assigned to a work order, both persisted
	 * WHEN: the work order is reloaded from the database
	 * THEN: the reloaded work order's mechanic equals the given mechanic
	 */
	@Test
	void testWorkOrderRecoversMechanic() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( mechanic, restored.getMechanic() );
	}

}