package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.YearMonth;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCREDITCARDS")
@DiscriminatorValue("CreditCard")
public class CreditCard extends PaymentMean {

	@Embedded
	@AttributeOverride(name = "number", column = @Column(name = "NUMBER", unique = true))
	private CreditCardNumber number;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "VALIDTHRU")
    private LocalDate validThru;

    // ---- Constructors ----
    protected CreditCard() {}

    public CreditCard(CreditCardNumber number) {
        this(number, "UNKNOWN", YearMonth.now().plusMonths(1));
    }

    public CreditCard(CreditCardNumber number, String type, YearMonth validThru) {
        ArgumentChecks.isNotNull(number, "card number cannot be null");
        ArgumentChecks.isNotBlank(type, "card type cannot be null or blank");
        ArgumentChecks.isNotNull(validThru, "validThru cannot be null");

        this.number = number;
        this.type = type;
        this.validThru = validThru.atEndOfMonth(); // internal conversion for mapping simplicity
    }

    // ---- Getters ----
    public CreditCardNumber getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public YearMonth getValidThru() {
        return YearMonth.from(validThru);
    }

    // ---- Business methods ----
    public boolean isValidNow() {
    	return ! validThru.isBefore( LocalDate.now() ); // validThru >= today, today included
    }

	/**
	 * A credit card can pay if is not outdated and the amount is positive. 
	 */
    @Override
    public boolean canBeCharged(Money amount) {
    	checkPositive(amount);
    	return isValidNow();
    }

    @Override
    public String toString() {
        return "CreditCard [number=" + number + ", type=" + type + ", validThru=" + validThru + "]";
    }
}
