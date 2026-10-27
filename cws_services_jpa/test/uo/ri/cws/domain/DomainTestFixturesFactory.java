package uo.ri.cws.domain;

import java.time.LocalDateTime;

import uo.ri.cws.domain.types.Money;

/**
 * This class provides factory methods to create domain objects for testing purposes.
 * 
 * The objects created by this class have their constructors with package visibility
 * so that they can only be instantiated from within the same package. 
 * This is done to allow persistence tests to create objects without exposing the constructors to the public API.
 */
public final class DomainTestFixturesFactory {

	private DomainTestFixturesFactory() {}

	public static Charge newCharge(Invoice invoice, PaymentMean paymentMean, Money amount) {
		return new Charge(invoice, paymentMean, amount);
	}

	public static Intervention newIntervention(Mechanic mechanic, WorkOrder workOrder, int minutes) {
		return new Intervention(mechanic, workOrder, minutes);
	}

	public static Intervention newIntervention(Mechanic mechanic, WorkOrder workOrder, LocalDateTime date,
			int minutes) {
		return new Intervention(mechanic, workOrder, date, minutes);
	}

	public static Substitution newSubstitution(SparePart sparePart, Intervention intervention, int quantity) {
		return new Substitution(sparePart, intervention, quantity);
	}

}
