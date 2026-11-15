package uo.ri.cws.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TSPAREPARTS")
public class SparePart extends BaseEntity {
	
	// Natural attributes
	@Column(name = "CODE", unique = true)
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;

	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "PRICE", precision = 19, scale = 2))
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
