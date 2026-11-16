package uo.ri.cws.domain;

import java.time.LocalDate;
import java.time.YearMonth;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class CreditCard extends PaymentMean {

	private CreditCardNumber number;

    private String type;

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
