package uo.ri.cws.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TVOUCHERS")
@DiscriminatorValue("Voucher")
public class Voucher extends PaymentMean {

    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "CODE", unique = true))
    private VoucherCode code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "AVAILABLE", precision = 19, scale = 2))
    private Money availableAmount; // cents

    // ---- Constructors ----
    protected Voucher() {}

    public Voucher(VoucherCode code, Money available) {
        this(code, "no-description", available);
    }

    public Voucher(VoucherCode code, String description, Money available) {
        ArgumentChecks.isNotNull(code, "Code cannot be null");
        ArgumentChecks.isNotBlank(description, "Description cannot be blank");
        ArgumentChecks.isNotNull(available, "Available cannot be null");
        ArgumentChecks.isTrue(available.isNonNegative(), "Available amount must be non-negative");

        this.code = code;
        this.description = description;
		this.availableAmount = Money.calc(available).toMoneyRoundedToCents();
    }

    // ------ Getters and setters (if needed) ----

    public Money getAvailableAmount() {
        return availableAmount;
    }

    public String getDescription() {
        return description;
    }

    public void changeDescription(String description) {
        ArgumentChecks.isNotBlank(description);
        this.description = description;
    }

    public VoucherCode getCode() {
        return code;
    }

    // ---- Business methods ----

    @Override
    /* package */ void charge(Money amount) {
    	Money normalized = normalizePositive(amount);
    	StateChecks.isTrue(canBeCharged(normalized), "Voucher has insufficient funds");
    	super.charge(normalized);
    	this.availableAmount = Money.calc(this.availableAmount).subtract(normalized).toMoneyRoundedToCents();
    }

	@Override
	/* package */ void rewind(Money amount) {
		Money normalized = normalizePositive(amount);
		super.rewind(normalized);
		this.availableAmount = Money.calc(this.availableAmount).add(normalized).toMoneyRoundedToCents();
	}

    @Override
    public boolean canBeCharged(Money amount) {
    	checkPositive(amount);
    	Money normalized = normalizePositive(amount);
    	return availableAmount.greaterOrEqual(normalized);
    }

    @Override
    public String toString() {
        return "Voucher [code=" + code + ", description=" + description + ", availableAmount=" + availableAmount + "]";
    }

}
