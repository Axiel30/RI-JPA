package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - An invoice recovers its billed work orders after being persisted and reloaded
 * - A work order recovers its invoice after being persisted and reloaded
 */
class BillsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private Invoice invoice;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(new Nif("12345678Z"));
		
		workOrder = new WorkOrder(vehicle);
		workOrder.assignTo(mechanic);
		workOrder.finish();
		workOrder.approve();

		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(workOrder, vehicle, invoice, mechanic);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( workOrder, vehicle, invoice, mechanic );
		factory.close();
	}

	/**
	 * GIVEN: an invoice with a billed work order, both persisted
	 * WHEN: the invoice is reloaded from the database
	 * THEN: the reloaded invoice's work orders contains that work order
	 */
	@Test
	void testInvoiceRecoversWorkOrders() {

		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );

		assertFalse( restored.getWorkOrders().isEmpty() );
		assertEquals( 1, restored.getWorkOrders().size() );
	}

	/**
	 * GIVEN: an invoice with a billed work order, both persisted
	 * WHEN: the work order is reloaded from the database
	 * THEN: the reloaded work order's invoice equals the given invoice
	 */
	@Test
	void testWorkOrderRecoversVehicle() {

		WorkOrder restored = unitOfWork.findById( WorkOrder.class, workOrder.getId() );

		assertEquals( invoice, restored.getInvoice() );
	}

}