package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - An intervention with no time nor substitutions amounts 0.0 €
 * - An intervention with 60 minutes amounts the price of an labor hour
 * - The constructor receiving substitutions creates them from the supplied map
 * - An intervention with time and requested substitutions returns the right amount
 * - The date of an intervention must be stored truncated to milliseconds
 * - An intervention with negative minutes throws IllegalArgumentException
 * - An intervention with null work order throws IllegalArgumentException
 * - An intervention with null mechanic throws IllegalArgumentException
 */
class InterventionConstructorTests {

	private static final Money SPARE_PRICE = Money.of("100.0");
	private static final int SIXTY_MINS = 60;
	private static final Money PRICE_PER_HOUR = Money.of("50.0");

	private Mechanic mechanic;
	private WorkOrder workOrder;
	private Vehicle vehicle;
	private VehicleType vehicleType;

	@BeforeEach
	void setUp() {
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
		vehicleType = new VehicleType("coche", PRICE_PER_HOUR);
		Associations.Classifies.link(vehicleType, vehicle);

		workOrder = new WorkOrder(vehicle, "falla la junta la trocla");
		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}

	/**
	 * GIVEN: a work order and a mechanic 
	 * WHEN: we create an intervention 
	 * THEN: the intervention is correctly created
	 * AND: the timestamp is adjusted to milliseconds to avoid database round problems
	 */
	@Test
	void testAmountsZero() {
		Intervention i = new Intervention(mechanic, workOrder, 0);

		assertEquals( Money.of("0.0"), i.getTotalAmount() );
		LocalDateTime date = i.getDate().truncatedTo( ChronoUnit.MILLIS );
		assertEquals( date, i.getDate() );
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with 60 minutes
	 * THEN: the intervention amounts the price of an labor hour
	 */
	@Test
	void testAmountOneHour() {
		Intervention i = new Intervention(mechanic, workOrder, SIXTY_MINS);

		assertEquals( vehicleType.getPricePerHour(), i.getTotalAmount() );
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with just one spare part
	 * THEN: the intervention amounts the price of the spare part
	 */
	@Test
	void testSparePartAmount() {
		SparePart sp = new SparePart("R1001", "junta la trocla", SPARE_PRICE );
		Intervention i = new Intervention(
				mechanic,
				workOrder,
				LocalDateTime.now(),
				0,
				Map.of(sp, 1)
			);

		assertEquals( sp.getPrice(), i.getTotalAmount() );
		assertEquals(1, i.getSubstitutions().size());
		Substitution substitution = i.getSubstitutions().iterator().next();
		assertEquals(1, substitution.getQuantity());
		assertSame(sp, substitution.getSparePart());
		assertSame(i, substitution.getIntervention());
	}

	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with time and spare parts
	 * THEN: the intervention returns the right amount
	 */
	@Test
	void testSpareAndTimeAmount() {
		int numSpareParts = 2;
		int timeForIntervention = SIXTY_MINS;
		
		SparePart r = new SparePart("R1001", "junta la trocla", SPARE_PRICE );
		Intervention i = new Intervention(
				mechanic,
				workOrder,
				LocalDateTime.now(),
				timeForIntervention,
				Map.of(r, numSpareParts)
			);

		Money laborAmount = Money.calc(PRICE_PER_HOUR)
				.times( timeForIntervention )   	// PRICE_PER_HOUR * 60
				.div( SIXTY_MINS )              	// / 60 mins, 1 hour
				.toMoneyExact();
		Money twoSparePartsAmount = Money.calc(SPARE_PRICE).times( numSpareParts ).toMoneyExact();

	    Money expected = Money.calc( laborAmount)
	                 .add( twoSparePartsAmount )
	                 .toMoneyExact();
	    
		assertEquals( expected, i.getTotalAmount() );
	}
	
	/**
	 * GIVEN: a work order and a mechanic
	 * WHEN: we create an intervention with negative minutes
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNegativeMinutes() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(mechanic, workOrder, -1)
			);
	}

	/**
	 * GIVEN: a work order 
	 * WHEN: we create an intervention with null work order
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNullWorkOrder() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(mechanic, null, 60)
			);
	}
	
	/**
	 * GIVEN: a mechanic 
	 * WHEN: we create an intervention with null mechanic
	 * THEN: it throws IllegalArgumentException
	 */
	@Test
	void testInterventionThrowsExceptionIfNullMechanic() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Intervention(null, workOrder, 60)
			);
	}
	
}
