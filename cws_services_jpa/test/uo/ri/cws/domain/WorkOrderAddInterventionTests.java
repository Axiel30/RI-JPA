package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Adding without a mechanic uses the assigned mechanic in ASSIGNED state
 * - Adding with an explicit mechanic accepts ASSIGNED and FINISHED states
 * - The explicit mechanic need not be the assigned mechanic
 * - Each overload without substitutions creates an empty substitution set
 * - A substitution map creates and links all requested substitutions
 * - Null maps and null entries are rejected atomically
 * - Zero and negative substitution quantities are rejected atomically
 * - Null mechanic, null date and negative minutes are rejected
 * - Interventions with duplicate natural identity are rejected
 * - Work order amount remains zero until approval
 */
class WorkOrderAddInterventionTests {

	private static final LocalDateTime DATE = LocalDateTime.of(2026, 9, 2, 10, 30, 15, 123_456_789);
	private static final Money HOURLY_RATE = Money.of("50.00");

	private WorkOrder workOrder;
	private Mechanic assignedMechanic;
	private Mechanic anotherMechanic;
	private SparePart firstSpare;
	private SparePart secondSpare;

	@BeforeEach
	void setUp() {
		Vehicle vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		VehicleType vehicleType = new VehicleType("car", HOURLY_RATE);
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		assignedMechanic = new Mechanic(
				new Nif("12345678Z"),
				new FullName("Assigned", "Mechanic")
			);
		anotherMechanic = new Mechanic(
				new Nif("87654321X"),
				new FullName("Another", "Mechanic")
			);
		firstSpare = new SparePart("R1001", "first spare", Money.of("100.00"));
		secondSpare = new SparePart("R1002", "second spare", Money.of("25.00"));
	}

	/**
	 * GIVEN: a work order assigned to a mechanic
	 * WHEN: adding an intervention without specifying a mechanic
	 * THEN: the assigned mechanic is used
	 * AND: the date is truncated to milliseconds
	 * AND: the intervention is linked with the work order
	 */
	@Test
	void testAddUsesAssignedMechanic() {
		workOrder.assignTo(assignedMechanic);

		workOrder.addIntervention(DATE, 60);

		Intervention intervention = onlyInterventionOf(workOrder);
		assertSame(assignedMechanic, intervention.getMechanic());
		assertSame(workOrder, intervention.getWorkOrder());
		assertEquals(DATE.truncatedTo(ChronoUnit.MILLIS), intervention.getDate());
		assertEquals(60, intervention.getMinutes());
		assertTrue(intervention.getSubstitutions().isEmpty());
		assertEquals(Money.ZERO, workOrder.getAmount());
	}

	/**
	 * GIVEN: an assigned work order and a different mechanic
	 * WHEN: adding an intervention with that explicit mechanic
	 * THEN: the explicit mechanic is used
	 */
	@Test
	void testExplicitMechanicInAssignedState() {
		workOrder.assignTo(assignedMechanic);

		workOrder.addIntervention(anotherMechanic, DATE, 60);

		assertSame(anotherMechanic, onlyInterventionOf(workOrder).getMechanic());
	}

	/**
	 * GIVEN: a finished work order
	 * WHEN: the foreman adds an intervention with an explicit mechanic
	 * THEN: the intervention is created for that mechanic
	 */
	@Test
	void testExplicitMechanicInFinishedState() {
		workOrder.assignTo(assignedMechanic);
		workOrder.finish();

		workOrder.addIntervention(anotherMechanic, DATE, 60);

		assertSame(anotherMechanic, onlyInterventionOf(workOrder).getMechanic());
		assertTrue(workOrder.isFinished());
		assertEquals(Money.ZERO, workOrder.getAmount());
	}

	/**
	 * GIVEN: an assigned work order and a substitution map
	 * WHEN: adding an intervention
	 * THEN: every substitution is created and linked
	 * AND: prices and the derived total are correct
	 */
	@Test
	void testAddWithSubstitutions() {
		workOrder.assignTo(assignedMechanic);

		workOrder.addIntervention(
				DATE,
				60,
				Map.of(firstSpare, 2, secondSpare, 3)
			);

		Intervention intervention = onlyInterventionOf(workOrder);
		assertEquals(2, intervention.getSubstitutions().size());
		assertSubstitution(intervention, firstSpare, 2, Money.of("100.00"));
		assertSubstitution(intervention, secondSpare, 3, Money.of("25.00"));
		assertEquals(Money.of("325.00"), intervention.getTotalAmount());
	}

	/**
	 * GIVEN: an assigned work order
	 * WHEN: adding an intervention with an empty substitution map
	 * THEN: the intervention is created without substitutions
	 */
	@Test
	void testEmptySubstitutionMapIsAccepted() {
		workOrder.assignTo(assignedMechanic);

		workOrder.addIntervention(DATE, 60, Map.of());

		assertTrue(onlyInterventionOf(workOrder).getSubstitutions().isEmpty());
	}

	/**
	 * GIVEN: an assigned work order
	 * WHEN: adding an intervention with a null substitution map
	 * THEN: an IllegalArgumentException is thrown and no intervention is created
	 */
	@Test
	void testNullSubstitutionMapIsRejectedAtomically() {
		workOrder.assignTo(assignedMechanic);

		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(DATE, 60, null)
			);

		assertTrue(workOrder.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: a substitution map containing a null spare part or quantity
	 * WHEN: adding an intervention
	 * THEN: an IllegalArgumentException is thrown and nothing is created
	 */
	@Test
	void testNullSubstitutionEntryIsRejectedAtomically() {
		workOrder.assignTo(assignedMechanic);
		Map<SparePart, Integer> substitutions = new HashMap<>();
		substitutions.put(null, 1);

		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(DATE, 60, substitutions)
			);
		assertTrue(workOrder.getInterventions().isEmpty());

		substitutions.clear();
		substitutions.put(firstSpare, null);
		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(DATE, 60, substitutions)
			);
		assertTrue(workOrder.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: a map containing one valid and one non-positive substitution
	 * WHEN: adding an intervention
	 * THEN: the whole operation is rejected without creating any object
	 */
	@Test
	void testNonPositiveQuantityIsRejectedAtomically() {
		workOrder.assignTo(assignedMechanic);
		Map<SparePart, Integer> substitutions = new LinkedHashMap<>();
		substitutions.put(firstSpare, 1);
		substitutions.put(secondSpare, 0);

		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(DATE, 60, substitutions)
			);
		assertTrue(workOrder.getInterventions().isEmpty());

		substitutions.put(secondSpare, -1);
		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(DATE, 60, substitutions)
			);
		assertTrue(workOrder.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: an assigned work order
	 * WHEN: adding with a null mechanic, null date or negative minutes
	 * THEN: an IllegalArgumentException is thrown and nothing is created
	 */
	@Test
	void testInvalidInterventionArgumentsAreRejected() {
		workOrder.assignTo(assignedMechanic);

		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(null, DATE, 60)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(assignedMechanic, null, 60)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> workOrder.addIntervention(assignedMechanic, DATE, -1)
			);
		assertTrue(workOrder.getInterventions().isEmpty());
	}

	/**
	 * GIVEN: an assigned work order with an intervention
	 * WHEN: adding another with the same work order, mechanic and millisecond
	 * THEN: an IllegalStateException is thrown and the original is preserved
	 */
	@Test
	void testDuplicateNaturalIdentityIsRejected() {
		workOrder.assignTo(assignedMechanic);
		workOrder.addIntervention(DATE, 60);
		LocalDateTime sameMillisecond = DATE.plusNanos(100_000);

		assertThrows(
				IllegalStateException.class,
				() -> workOrder.addIntervention(sameMillisecond, 30)
			);

		assertEquals(1, workOrder.getInterventions().size());
		assertEquals(60, onlyInterventionOf(workOrder).getMinutes());
	}

	private Intervention onlyInterventionOf(WorkOrder target) {
		assertEquals(1, target.getInterventions().size());
		return target.getInterventions().iterator().next();
	}

	private void assertSubstitution(
			Intervention intervention,
			SparePart sparePart,
			int quantity,
			Money price) {
		Substitution substitution = intervention.getSubstitutions().stream()
				.filter(s -> s.getSparePart().equals(sparePart))
				.findFirst()
				.orElseThrow();

		assertEquals(quantity, substitution.getQuantity());
		assertEquals(price, substitution.getPrice());
		assertSame(intervention, substitution.getIntervention());
		assertSame(sparePart, substitution.getSparePart());
	}
}
