package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Scenarios:
 * - Linking an invoice and a work order updates both sides of the association
 * - Unlinking an invoice and a work order removes the link on both sides
 * - The collection returned by getWorkOrders() is a safe copy
 * - Linking rejects a null invoice
 * - Linking rejects a null work order
 * - Linking rejects a work order that already has an invoice
 * - Linking rejects a work order already present in the invoice's work orders
 * - Unlinking rejects a null work order
 * - Unlinking rejects a work order without an invoice
 * - Unlinking rejects an inconsistent association
 */
class AssociationBillsTests {
	private WorkOrder workOrder;
	private Invoice invoice;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneWorkOrder();
		invoice = new Invoice(0L, LocalDate.now());
	}
	
	/**
	 * GIVEN: an invoice and a work order linked through Associations.Bills
	 * WHEN: querying both objects
	 * THEN: the work order is in the invoice's work orders
	 * AND: the invoice is the work order's invoice
	 */
	@Test
	void testLinkOnInvoice() {
		Associations.Bills.link(invoice, workOrder);

		assertTrue( invoice.getWorkOrders().contains( workOrder ));
		assertSame( invoice, workOrder.getInvoice());
	}

	/**
	 * GIVEN: an invoice and a work order linked
	 * WHEN: Associations.Bills.unlink is called
	 * THEN: the work order is removed from the invoice's work orders
	 * AND: the work order's invoice is null
	 */
	@Test
	void testUnlinkOnInvoice() {
		Associations.Bills.link(invoice, workOrder);

		Associations.Bills.unlink(workOrder);

		assertTrue( invoice.getWorkOrders().isEmpty() );
		assertNull( workOrder.getInvoice());
	}
	
	/**
	 * GIVEN: an invoice with a linked work order
	 * WHEN: the returned work orders set is modified
	 * THEN: the original association is not affected (it must be a copy or a read-only version)
	 */
	@Test
	void testSafeReturn() {
		Associations.Bills.link(invoice, workOrder);

		Set<WorkOrder> returned = invoice.getWorkOrders();
		returned.remove( workOrder );

		assertTrue( returned.isEmpty() );
		assertEquals( 1, invoice.getWorkOrders().size(), 
				"It must be a copy of the collection or a read-only version" 
		);
	}

	/**
	 * GIVEN: a linked work order
	 * WHEN: linking it with a null invoice
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and invoice remain unlinked
	 */
	@Test
	void testLinkNullInvoice() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Bills.link(null, workOrder)
		);

		assertNull(workOrder.getInvoice());
		assertTrue(invoice.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: an invoice
	 * WHEN: linking it with a null work order
	 * THEN: an IllegalArgumentException is thrown
	 * AND: its work orders remain unchanged
	 */
	@Test
	void testLinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Bills.link(invoice, null)
		);

		assertTrue(invoice.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order already billed by an invoice
	 * WHEN: linking it to another invoice
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkWorkOrderWithInvoice() {
		Invoice another = new Invoice(1L);
		Associations.Bills.link(invoice, workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Bills.link(another, workOrder)
		);

		assertSame(invoice, workOrder.getInvoice());
		assertTrue(invoice.getWorkOrders().contains(workOrder));
		assertTrue(another.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order present in an invoice but without its invoice end
	 * WHEN: linking both objects
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkWorkOrderAlreadyInInvoice() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		invoice._getWorkOrders().add(workOrder);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Bills.link(invoice, workOrder)
		);

		assertNull(workOrder.getInvoice());
		assertTrue(invoice.getWorkOrders().contains(workOrder));
	}

	/**
	 * GIVEN: no work order
	 * WHEN: Associations.Bills.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the work order and invoice remain unlinked
	 */
	@Test
	void testUnlinkNullWorkOrder() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Bills.unlink(null)
		);

		assertNull(workOrder.getInvoice());
		assertTrue(invoice.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order without an invoice
	 * WHEN: Associations.Bills.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the work order remains unbilled
	 */
	@Test
	void testUnlinkWorkOrderWithoutInvoice() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Bills.unlink(workOrder)
		);

		assertNull(workOrder.getInvoice());
		assertTrue(invoice.getWorkOrders().isEmpty());
	}

	/**
	 * GIVEN: a work order whose invoice does not contain it
	 * WHEN: Associations.Bills.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentAssociation() {
		// Test-only backdoor: creates an inconsistent association end.
		workOrder._setInvoice(invoice);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Bills.unlink(workOrder)
		);

		assertSame(invoice, workOrder.getInvoice());
		assertFalse(invoice.getWorkOrders().contains(workOrder));
	}
}
