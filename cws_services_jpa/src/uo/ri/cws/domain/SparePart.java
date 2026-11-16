package uo.ri.cws.domain;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class SparePart extends BaseEntity {
	
	// Natural attributes
	private String code;

	private String description;

	private Money unitPrice;

	protected SparePart() {}

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
	public String toString() {
		return "SparePart[code=" + code + ", description=" + description + ", unitPrice=" + unitPrice + "]";
	}

}
