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
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of an invoice are persisted and can be reloaded correctly
 * - An invoice cannot be persisted if another one with the same number already exists
 */
class InvoiceMappingTests {

	private EntityManagerFactory factory;
	private UnitOfWork unitOfWork;

	private Invoice invoice;
	private Vehicle vehicle;
	private WorkOrder workOrder;
	private Mechanic mechanic;

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
		workOrder.approve();
		
		invoice = new Invoice( 1L );
		invoice.addWorkOrder(workOrder);

		unitOfWork.persist(vehicle, invoice, workOrder, mechanic);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( invoice, vehicle, workOrder, mechanic );
		factory.close();
	}

	/**
	 * GIVEN: an invoice persisted with a billed work order
	 * WHEN: the invoice is reloaded from the database
	 * THEN: all its fields (id, number, date, amounts, state and work orders) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
	    Invoice restored = unitOfWork.findById(Invoice.class, invoice.getId());

	    assertEquals(invoice.getId(), restored.getId());
	    assertEquals(invoice.getNumber(), restored.getNumber());
	    assertEquals(invoice.getDate(), restored.getDate());

	    // amounts
	    assertEquals(invoice.getSubtotal(), restored.getSubtotal());
	    assertEquals(invoice.getVatRate(), restored.getVatRate());
	    assertEquals(invoice.getVatAmount(), restored.getVatAmount());
	    assertEquals(invoice.getTotal(), restored.getTotal());

	    // state
	    assertEquals(invoice.getState(), restored.getState());

	    // work orders
	    assertEquals(invoice.getWorkOrders(), restored.getWorkOrders());
	}

//	@Test
//	void testAllFieldsPersisted() {
//		Invoice restored = unitOfWork.findById( Invoice.class, invoice.getId() );
//
//		assertEquals( invoice.getId(), restored.getId() );
//		assertEquals( invoice.getNumber(), restored.getNumber() );
//		assertEquals( invoice.getDate(), restored.getDate() );
//		assertEquals( invoice.getTotal(), restored.getTotal() );
//		assertEquals( invoice.getVat(), restored.getVat() );
//		assertEquals( invoice.getState(), restored.getState() );
//		assertEquals( invoice.getWorkOrders(), restored.getWorkOrders() );
//	}

	/**
	 * GIVEN: a persisted invoice
	 * WHEN: a new invoice with the same number is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		Invoice repeatedInvoice = new Invoice( invoice.getNumber() );

		assertThrows( PersistenceException.class, () -> {
			unitOfWork.persist( repeatedInvoice );
		});
	}

}