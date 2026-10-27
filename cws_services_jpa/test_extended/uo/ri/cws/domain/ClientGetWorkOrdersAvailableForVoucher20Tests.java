package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;

/**
 * Scenarios:
 * - A client without work orders has no work orders available for a voucher
 * - Unused work orders included in paid invoices are available
 * - Work orders not included in an invoice are not available
 * - Work orders included in unpaid invoices are not available
 * - Work orders already used for a voucher are not available
 * - All valid work orders are returned among other unavailable or used work orders
 */
class ClientGetWorkOrdersAvailableForVoucher20Tests {
	/**
	 * GIVEN: a client without work orders
	 * WHEN: querying its work orders available for a 20 euro voucher
	 * THEN: an empty list is returned
	 */
	@Test
	void testNoWorkOrders() {
		Client client = new ClientBuilder().build();

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a client with two unused work orders included in paid invoices
	 * WHEN: querying the available work orders
	 * THEN: both work orders are returned
	 */
	@Test
	void testUnusedWorkOrdersInPaidInvoicesAreAvailable() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(2)
			.build();
		List<WorkOrder> expected = workOrdersOf(client);

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertEquals(Set.copyOf(expected), Set.copyOf(result));
	}

	/**
	 * GIVEN: a client with an approved work order not included in an invoice
	 * WHEN: querying the available work orders
	 * THEN: the work order is not returned
	 */
	@Test
	void testUninvoicedWorkOrderIsNotAvailable() {
		Client client = new ClientBuilder()
			.withUninvoicedWorkOrder()
			.build();

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a client with a work order included in an unpaid invoice
	 * WHEN: querying the available work orders
	 * THEN: the work order is not returned
	 */
	@Test
	void testWorkOrderInUnpaidInvoiceIsNotAvailable() {
		Client client = new ClientBuilder()
			.withUnpaidInvoice()
			.build();

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a work order included in a paid invoice
	 * AND: the work order has already been used for a voucher
	 * WHEN: querying the available work orders
	 * THEN: the work order is not returned
	 */
	@Test
	void testUsedWorkOrderIsNotAvailable() {
		Client client = new ClientBuilder()
			.withPaidInvoice()
			.build();
		WorkOrder workOrder = workOrdersOf(client).get(0);
		workOrder.markAsUsedForVoucher();

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a client with valid, unpaid, uninvoiced and used work orders
	 * WHEN: querying the available work orders
	 * THEN: all and only the valid work orders are returned
	 */
	@Test
	void testOnlyValidWorkOrdersAreReturned() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(3)
			.withUnpaidWorkOrder()
			.withUninvoicedWorkOrder()
			.build();
		List<WorkOrder> paid = workOrdersOf(client).stream()
			.filter(workOrder -> workOrder.getInvoice() != null)
			.filter(workOrder -> workOrder.getInvoice().isSettled())
			.toList();
		WorkOrder used = paid.get(0);
		used.markAsUsedForVoucher();
		Set<WorkOrder> expected = Set.copyOf(paid.subList(1, paid.size()));

		List<WorkOrder> result = client.getWorkOrdersAvailableForVoucher20();

		assertEquals(expected, Set.copyOf(result));
	}

	private List<WorkOrder> workOrdersOf(Client client) {
		return client.getVehicles().stream()
			.flatMap(vehicle -> vehicle.getWorkOrders().stream())
			.toList();
	}
}
