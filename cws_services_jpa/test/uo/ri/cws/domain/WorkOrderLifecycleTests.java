package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.WorkOrder.WorkOrderState;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;

/**
 * Integration tests for the complete WorkOrder state machine.
 *
 * Scenarios:
 * - assignTo is accepted only in OPEN
 * - unassign and finish are accepted only in ASSIGNED
 * - approve and reopen are accepted only in FINISHED
 * - unapprove and invoice are accepted only in APPROVED
 * - uninvoice is accepted only in INVOICED
 * - addIntervention with the assigned mechanic is accepted only in ASSIGNED
 * - addIntervention with an explicit mechanic is accepted in ASSIGNED and FINISHED
 * - removeIntervention is accepted in ASSIGNED and FINISHED
 */
class WorkOrderLifecycleTests {

	private static final LocalDateTime INTERVENTION_DATE = LocalDateTime.of(2026, 9, 2, 10, 30);

	private Mechanic mechanic;
	private int sequence;

	@BeforeEach
	void setUp() {
		mechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
	}

	/**
	 * GIVEN: work orders in every state except OPEN
	 * WHEN: assigning them to a mechanic
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testAssignToIsRejectedOutsideOpen() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.OPEN),
				workOrder -> workOrder.assignTo(mechanic)
			);
	}

	/**
	 * GIVEN: work orders in every state except ASSIGNED
	 * WHEN: unassigning them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testUnassignIsRejectedOutsideAssigned() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.ASSIGNED),
				WorkOrder::unassign
			);
	}

	/**
	 * GIVEN: work orders in every state except ASSIGNED
	 * WHEN: finishing them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testFinishIsRejectedOutsideAssigned() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.ASSIGNED),
				WorkOrder::finish
			);
	}

	/**
	 * GIVEN: work orders in every state except FINISHED
	 * WHEN: approving them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testApproveIsRejectedOutsideFinished() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.FINISHED),
				WorkOrder::approve
			);
	}

	/**
	 * GIVEN: work orders in every state except FINISHED
	 * WHEN: reopening them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testReopenIsRejectedOutsideFinished() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.FINISHED),
				WorkOrder::reopen
			);
	}

	/**
	 * GIVEN: work orders in every state except APPROVED
	 * WHEN: unapproving them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testUnapproveIsRejectedOutsideApproved() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.APPROVED),
				WorkOrder::unapprove
			);
	}

	/**
	 * GIVEN: work orders in every state except APPROVED
	 * WHEN: invoicing them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testInvoiceIsRejectedOutsideApproved() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.APPROVED),
				WorkOrder::invoice
			);
	}

	/**
	 * GIVEN: work orders in every state except INVOICED
	 * WHEN: uninvoicing them
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testUninvoiceIsRejectedOutsideInvoiced() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.INVOICED),
				WorkOrder::uninvoice
			);
	}

	/**
	 * GIVEN: work orders in every state except ASSIGNED
	 * WHEN: adding an intervention with the implicitly assigned mechanic
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testImplicitMechanicInterventionIsRejectedOutsideAssigned() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.ASSIGNED),
				workOrder -> workOrder.addIntervention(INTERVENTION_DATE, 60)
			);
	}

	/**
	 * GIVEN: work orders outside ASSIGNED and FINISHED
	 * WHEN: adding an intervention with an explicit mechanic
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testExplicitMechanicInterventionIsRejectedOutsideAssignedAndFinished() {
		assertRejectedExcept(
				EnumSet.of(WorkOrderState.ASSIGNED, WorkOrderState.FINISHED),
				workOrder -> workOrder.addIntervention(
						mechanic,
						INTERVENTION_DATE,
						60
					)
			);
	}

	/**
	 * GIVEN: work orders outside ASSIGNED and FINISHED with an intervention
	 * WHEN: removing that intervention
	 * THEN: every operation is rejected without changing its state
	 */
	@Test
	void testRemoveInterventionIsRejectedOutsideAssignedAndFinished() {
		for (WorkOrderState state : rejectedStatesExcept(EnumSet.of(
				WorkOrderState.ASSIGNED,
				WorkOrderState.FINISHED))) {
			WorkOrder workOrder = workOrderIn(state);

			Intervention intervention = DomainTestMocksFactory.oneIntervention();
			Associations.RecordsIntervenes.atomicLink(
					workOrder,
					intervention,
					mechanic
				);

			assertThrows(
					IllegalStateException.class,
					() -> workOrder.removeIntervention(intervention),
					"State " + state
				);
			assertEquals(state, workOrder.getState());
		}
	}

	private void assertRejectedExcept(Set<WorkOrderState> acceptedStates, Consumer<WorkOrder> operation) {
		for (WorkOrderState state : rejectedStatesExcept(acceptedStates)) {
			WorkOrder workOrder = workOrderIn(state);

			assertThrows(
					IllegalStateException.class,
					() -> operation.accept(workOrder),
					"State " + state
				);
			assertEquals(state, workOrder.getState());
		}
	}

	private Set<WorkOrderState> rejectedStatesExcept(Set<WorkOrderState> acceptedStates) {
		Set<WorkOrderState> rejected = EnumSet.allOf(WorkOrderState.class);
		rejected.removeAll(acceptedStates);
		return rejected;
	}

	private WorkOrder workOrderIn(WorkOrderState state) {
		Vehicle vehicle = new Vehicle(
				new LicensePlate(String.format("%04d TST", sequence++)),
				"model",
				"make"
			);
		WorkOrder workOrder = new WorkOrder(vehicle);

		if (state == WorkOrderState.OPEN) {
			return workOrder;
		}

		workOrder.assignTo(mechanic);
		if (state == WorkOrderState.ASSIGNED) {
			return workOrder;
		}

		workOrder.finish();
		if (state == WorkOrderState.FINISHED) {
			return workOrder;
		}

		workOrder.approve();
		if (state == WorkOrderState.APPROVED) {
			return workOrder;
		}

		Invoice invoice = new Invoice(sequence);
		invoice.addWorkOrder(workOrder);
		return workOrder;
	}
}
