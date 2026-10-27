package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 * 
 * Scenarios:
 * - An approved work order can be marked as invoiced when it has an invoice attached
 * - An approved work order cannot be marked as invoiced if it has no invoice attached, throws ISE
 */
class WorkOrderInvoiceTests {

	private static final Money HOURLY_RATE = Money.of("50.0");
	
	private Vehicle vehicle;
	private Mechanic mechanic;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "ibiza", "seat");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
		
	}
	
	/**
	 * GIVEN: A work order in state APPROVED with an invoice attached 
	 * WHEN: marking it as invoiced 
	 * THEN: the work order state is INVOICED
	 */
	@Test
	void testMarkAsInvoiced() {
		WorkOrder wo = createApprovedWorkOrder();
		
		Invoice invoice = new Invoice(1000L);
		Associations.Bills.link(invoice, wo);

		wo.invoice();
		
		assertTrue( wo.isInvoiced() );
		assertEquals(HOURLY_RATE, wo.getAmount());
	}
	
	/**
	 * GIVEN: A work order in state APPROVED without an invoice
	 * WHEN: marking it as invoiced
	 * THEN: an IllegalStateException is thrown
	 */ 
	 @Test
	 void testMarkAsInvoicedFromApprovedWorkOrderWithoutInvoice() {
		 WorkOrder wo = createApprovedWorkOrder();

         assertThrows(IllegalStateException.class, () -> wo.invoice());
         assertTrue( wo.isApproved() );
		 assertEquals(HOURLY_RATE, wo.getAmount());
     }
	 
	private WorkOrder createApprovedWorkOrder() {
		WorkOrder wo = new WorkOrder(vehicle);
		wo.assignTo(mechanic);
		Intervention intervention = DomainTestMocksFactory.oneInterventionWithAmount(HOURLY_RATE);
		// Test-only backdoor: invoicing is isolated from addIntervention().
		wo._getInterventions().add(intervention);
		wo.finish();
		wo.approve();
		return wo;
	}
     
}
