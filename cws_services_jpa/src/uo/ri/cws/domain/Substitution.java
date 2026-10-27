package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

/**
 * A substitution is a relation between an intervention and a spare part. 
 * It represents the fact that a spare part has been used in an intervention.
 * 
 * Internal aggregate class of WorkOrder. Only Intervention can create Substitutions (through WorkOrder).
 */
public class Substitution {
	// Natural attributes
    private Money price;
	private int quantity;

	// Accidental attributes
	private SparePart sparePart;
	private Intervention intervention;
    

	// ---------------------------- Constructors -----------------------------
	/**
	 * Package-private constructor to create a new Substitution with the given parameters.
	 * Only Intervention can create Substitutions.
	 */
	/* package */ Substitution(SparePart sparePart, Intervention intervention, int quantity) {
		// Validate input parameters
		ArgumentChecks.isNotNull(sparePart, "Spare part must not be null");
		ArgumentChecks.isNotNull(intervention, "Intervention must not be null");
		ArgumentChecks.isTrue(quantity > 0, "Quantity must be positive");

		this.price = sparePart.getUnitPrice();
		this.quantity = quantity;

		// Link the substitution with the intervention and spare part atomically
		Associations.PerformsSubstitutes.atomicLink(intervention, this, sparePart);
	}

	// ---------------------------- Association helpers -----------------------------
	
	void _setSparePart(SparePart sparePart) {
		this.sparePart = sparePart;
	}

	void _setIntervention(Intervention intervention) {
		this.intervention = intervention;
	}

	public SparePart getSparePart() {
		return sparePart;
	}

	public Intervention getIntervention() {
		return intervention;
	}

	public Money getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	// ---------------------------- Business methods -----------------------------
	public Money getTotalAmount() {
		return Money.calc(price).times(quantity).toMoneyRoundedToCents();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Substitution other = (Substitution) obj;
		return Objects.equals(sparePart, other.sparePart) && Objects.equals(intervention, other.intervention);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sparePart, intervention);
	}

}
