package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class SparePart {
	
	// Natural attributes
	private String code;
	private String description;
	private Money unitPrice;

	public SparePart(String code) {
		this( code, "no-description", Money.ZERO );
	}

	public SparePart(String code, String description, Money price) {
		ArgumentChecks.isNotNull(code, "Code must not be null");
		ArgumentChecks.isNotBlank(code, "Code must not be blank");
		ArgumentChecks.isNotNull(description, "Description must not be null");
		ArgumentChecks.isNotBlank(description, "Description must not be blank");
		ArgumentChecks.isNotNull(price, "Price must not be null");
		ArgumentChecks.isTrue(price.isNonNegative(), "Price must be non-negative");
		this.code = code;
		this.description = description;
		// Round the price to cents
		this.unitPrice = Money.calc(price).toMoneyRoundedToCents();
	}

	// ---- Getters ----
	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public Money getUnitPrice() {
		return unitPrice;
	}

	public Money getPrice() {
		return unitPrice;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		SparePart other = (SparePart) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public String toString() {
		return "SparePart[code=" + code + ", description=" + description + ", unitPrice=" + unitPrice + "]";
	}

}
