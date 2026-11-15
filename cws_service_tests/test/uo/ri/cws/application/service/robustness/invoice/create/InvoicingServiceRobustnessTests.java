package uo.ri.cws.application.service.robustness.invoice.create;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;

/**
 * Scenarios:
 *   - Trying to create an invoice with a null list of work order identifiers
 *   - Trying to create an invoice with an empty list of work order identifiers
 *   - Trying to create an invoice with a null work order identifier
 *   - Trying to find not invoiced work orders with an empty client NIF
 *   - Trying to find not invoiced work orders with a null client NIF
 */
class InvoicingServiceRobustnessTests {

	private final InvoicingService service = Factories.service.forCreateInvoiceService();

	/**
	 * Given a null list of work order identifiers
	 * When trying to create an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void createRejectsNullWorkOrderIds() {
		assertRejected(() -> service.create(null));
	}

	/**
	 * Given an empty list of work order identifiers
	 * When trying to create an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void createRejectsEmptyWorkOrderIds() {
		assertRejected(() -> service.create(List.of()));
	}

	/**
	 * Given a list containing a null work order identifier
	 * When trying to create an invoice
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void createRejectsNullWorkOrderId() {
		List<String> workOrderIds = new ArrayList<>(List.of("work-order-id"));
		workOrderIds.add(null);

		assertRejected(() -> service.create(workOrderIds));
	}

	/**
	 * Given an empty client NIF
	 * When trying to find not invoiced work orders
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void findNotInvoicedWorkOrdersRejectsEmptyNif() {
		assertRejected(() -> service.findNotInvoicedWorkOrdersByClientNif(""));
	}

	/**
	 * Given a null client NIF
	 * When trying to find not invoiced work orders
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void findNotInvoicedWorkOrdersRejectsNullNif() {
		assertRejected(() -> service.findNotInvoicedWorkOrdersByClientNif(null));
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
