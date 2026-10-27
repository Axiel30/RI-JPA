package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;

/**
 * Scenarios:
 * - Marking an unused work order for a voucher changes it to used
 */
class WorkOrderMarkAsUsedForVoucherTests {
	/**
	 * GIVEN: an unused work order included in a paid invoice
	 * AND: the work order is available for a voucher
	 * WHEN: marking the work order as used for a voucher
	 * THEN: it is no longer available for a voucher
	 */
	@Test
	void testMarkWorkOrderAsUsed() {
		Client client = new ClientBuilder()
			.withPaidInvoice()
			.build();
		WorkOrder workOrder = workOrdersOf(client).get(0);
		assertTrue(client.getWorkOrdersAvailableForVoucher20().contains(workOrder));

		workOrder.markAsUsedForVoucher();

		assertFalse(client.getWorkOrdersAvailableForVoucher20().contains(workOrder));
	}

	private List<WorkOrder> workOrdersOf(Client client) {
		return client.getVehicles().stream()
			.flatMap(vehicle -> vehicle.getWorkOrders().stream())
			.toList();
	}
}
