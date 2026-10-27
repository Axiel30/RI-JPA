package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;

/**
 * Scenarios:
 * - Exactly three available work orders are marked as used
 * - Only three work orders are marked when more than three are available
 * - Used or unavailable work orders are ignored when choosing the three work orders
 * - Trying to mark work orders when fewer than three are available is rejected without changing any work order
 */
class ClientMarkThreeWorkOrdersAsUsedForVoucher20Tests {
	/**
	 * GIVEN: a client with exactly three available work orders
	 * WHEN: marking three work orders as used for a voucher
	 * THEN: no work order remains available
	 */
	@Test
	void testMarkExactlyThreeWorkOrders() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(3)
			.build();

		client.markThreeWorkOrdersAsUsedForVoucher20();

		assertTrue(client.getWorkOrdersAvailableForVoucher20().isEmpty());
	}

	/**
	 * GIVEN: a client with four available work orders
	 * WHEN: marking three work orders as used for a voucher
	 * THEN: exactly one of the original work orders remains available
	 */
	@Test
	void testOnlyThreeWorkOrdersAreMarked() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(4)
			.build();
		Set<WorkOrder> original = Set.copyOf(
			client.getWorkOrdersAvailableForVoucher20()
		);

		client.markThreeWorkOrdersAsUsedForVoucher20();

		List<WorkOrder> remaining = client
			.getWorkOrdersAvailableForVoucher20();
		assertEquals(1, remaining.size());
		assertTrue(original.contains(remaining.get(0)));
	}

	/**
	 * GIVEN: three available work orders
	 * AND: one used, one unpaid and one uninvoiced work order
	 * WHEN: marking three work orders as used for a voucher
	 * THEN: no previously available work order remains available
	 * AND: the unpaid work order becomes available if its invoice is later paid
	 */
	@Test
	void testUsedAndUnavailableWorkOrdersAreIgnored() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(4)
			.withUnpaidWorkOrder()
			.withUninvoicedWorkOrder()
			.build();
		List<WorkOrder> workOrders = workOrdersOf(client);
		List<WorkOrder> paid = workOrders.stream()
			.filter(workOrder -> workOrder.getInvoice() != null)
			.filter(workOrder -> workOrder.getInvoice().isSettled())
			.toList();
		WorkOrder alreadyUsed = paid.get(0);
		alreadyUsed.markAsUsedForVoucher();
		WorkOrder unpaid = workOrders.stream()
			.filter(workOrder -> workOrder.getInvoice() != null)
			.filter(workOrder -> !workOrder.getInvoice().isSettled())
			.findFirst()
			.orElseThrow();

		client.markThreeWorkOrdersAsUsedForVoucher20();

		assertTrue(client.getWorkOrdersAvailableForVoucher20().isEmpty());
		settle(unpaid.getInvoice());
		assertEquals(
			List.of(unpaid),
			client.getWorkOrdersAvailableForVoucher20()
		);
	}

	/**
	 * GIVEN: a client with only two available work orders
	 * WHEN: trying to mark three work orders as used for a voucher
	 * THEN: an IllegalStateException is thrown
	 * AND: both work orders remain available
	 */
	@Test
	void testFewerThanThreeWorkOrdersAreRejected() {
		Client client = new ClientBuilder()
			.withPaidWorkOrders(2)
			.build();
		Set<WorkOrder> original = Set.copyOf(
			client.getWorkOrdersAvailableForVoucher20()
		);

		assertThrows(
			IllegalStateException.class,
			client::markThreeWorkOrdersAsUsedForVoucher20
		);

		assertEquals(
			original,
			Set.copyOf(client.getWorkOrdersAvailableForVoucher20())
		);
	}

	private List<WorkOrder> workOrdersOf(Client client) {
		return client.getVehicles().stream()
			.flatMap(vehicle -> vehicle.getWorkOrders().stream())
			.toList();
	}

	private void settle(Invoice invoice) {
		PaymentMean cash = new Cash(new ClientBuilder().build());
		invoice.settle(Map.of(cash, invoice.getTotal()));
	}
}
