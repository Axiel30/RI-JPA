package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Percentage;

/**
 * To better understand this tests, please review the WorkOrder state diagram
 * see the "project scope statement" document
 *
 * Scenarios: 
 * - Add a work order to an invoice, the amount is correctly computed
 * - Add two work orders to an invoice, the amount is correctly computed 
 * - Add a work order to an invoice, the work order state changes to INVOICED 
 * - Add two work orders to an invoice, both work orders change their state to INVOICED 
 * - Add a null work order to an invoice, throws IAE 
 * - If the date of the invoice is before is before 1/7/2012 the VAT (IVA) is 18%
 */
class InvoiceAddWorkOrderTests {

	private static final Money WO_AMOUNT = Money.of("100.0");
	private static final Percentage VAT_RATE = Percentage.ofPercent(21);

	private WorkOrder workOrder;

	@BeforeEach
	void setUp() {
		workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(WO_AMOUNT);
	}

	/**
	 * GIVEN: An invoice with no work orders 
	 * WHEN: adding a work order to the invoice 
	 * THEN: the amounts of the invoice are correct 
	 * AND: the work order and the invoice are linked 
	 * AND: the work order state changes to INVOICED 
	 * AND: the invoice state is ISSUED 
	 * AND: the work order markAsInvoiced method is invoked once
	 */
	@Test
	void testNewAmountAfterAddingWorkOrder() {
	    Invoice invoice = new Invoice(1000L);

	    invoice.addWorkOrder(workOrder);

	    // expected net
	    Money expectedSubTotal = WO_AMOUNT;

	    // expected vat
	    Money expectedVat = Money.calc(WO_AMOUNT)
	                 .times(VAT_RATE.asRatio())
	                 .toMoneyRoundedToCents();

	    // expected gross
	    Money expectedTotal = Money.calc(WO_AMOUNT)
	                 .add(expectedVat)
	                 .toMoneyRoundedToCents();

	    assertEquals(expectedSubTotal, invoice.getSubtotal());
	    assertEquals(VAT_RATE, invoice.getVatRate());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedTotal, invoice.getTotal());

	    assertTrue(invoice.getWorkOrders().contains(workOrder));
	    assertTrue(! invoice.isSettled());
	    verify(workOrder, times(1)).invoice();
	}

	/**
	 * GIVEN: An invoice with one work order 
	 * WHEN: adding another work order to the invoice 
	 * THEN: the amount of the invoice is the sum of the amounts of both work orders 
	 * AND: both work orders and the invoice are linked 
	 * AND: both work orders change their state to INVOICED
	 */
	@Test
	void testInvoiceAmountAddingTwoWorkOrders() {
	    Invoice invoice = new Invoice(0L);
	    WorkOrder anotherWo = createAnotherWorkOrder();

	    invoice.addWorkOrder(workOrder);
	    invoice.addWorkOrder(anotherWo);

	    // net = WO_AMOUNT * 2
	    Money expectedNet =
	            Money.calc(WO_AMOUNT)
	                 .times(BigDecimal.valueOf(2))
	                 .toMoneyRoundedToCents();

	    // vat = (WO_AMOUNT * VAT_RATE) * 2
	    Money expectedVat =
	            Money.calc(WO_AMOUNT)
	                 .times(VAT_RATE.asRatio())
	                 .times(BigDecimal.valueOf(2))
	                 .toMoneyRoundedToCents();

	    // gross = net + vat
	    Money expectedGross =
	            Money.calc(expectedNet)
	                 .add(expectedVat)
	                 .toMoneyRoundedToCents();

	    // ASSERTS
	    assertEquals(expectedNet, invoice.getSubtotal());
	    assertEquals(VAT_RATE, invoice.getVatRate());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedGross, invoice.getTotal());

	    assertTrue(! invoice.isSettled());
	    verify(workOrder, times(1)).invoice();
	    verify(anotherWo, times(1)).invoice();
	}

	/**
	 * GIVEN: An invoice with one work order 
	 * WHEN: adding a null work order 
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testAddNullWorkOrderThrowsIAE() {
		Invoice invoice = new Invoice(0L);

		assertThrows(IllegalArgumentException.class, () -> invoice.addWorkOrder(null));
	}

	/**
	 * GIVEN: An invoice with one work order 
	 * WHEN: the date of the invoice is before 1/7/2012 
	 * THEN: the VAT (IVA) is 18%
	 */
	@Test
	void testAmountForInvoicesPriorJuly2012() {
	    LocalDate JUNE_6_2012 = LocalDate.of(2012, 6, 15);

	    Invoice invoice = new Invoice(0L, JUNE_6_2012);
	    invoice.addWorkOrder(workOrder);

	    Percentage oldVat = Percentage.ofPercent(18);

	    // net
	    Money expectedNet = WO_AMOUNT;

	    // vat = WO_AMOUNT * 0.18
	    Money expectedVat =
	            Money.calc(WO_AMOUNT)
	                 .times(oldVat.asRatio())
	                 .toMoneyRoundedToCents();

	    // gross = net + vat
	    Money expectedGross =
	            Money.calc(expectedNet)
	                 .add(expectedVat)
	                 .toMoneyRoundedToCents();

	    // ASSERTS
	    assertEquals(expectedNet, invoice.getSubtotal());
	    assertEquals(oldVat, invoice.getVatRate());
	    assertEquals(expectedVat, invoice.getVatAmount());
	    assertEquals(expectedGross, invoice.getTotal());
	}

	private static WorkOrder createAnotherWorkOrder() {
		return DomainTestMocksFactory.oneApprovedWorkOrder(WO_AMOUNT);
	}

}
