package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public abstract class PaymentMean {

    // Natural attributes
    private Money accumulated = Money.ZERO; // cents

    // Accidental attributes
    private Client client;

    // ---- Association helpers ----

    void _setClient(Client client) {
        this.client = client;
    }

    // ---- Business methods ----

    public abstract boolean canBeCharged(Money amount);

    /**
     * Increments the accumulated amount of the payment mean by the given amount.
     * package-private Only called from Charge constructor.
     * 
     * @param amount
     */
    /* package */ void charge(Money amount) {
        Money normalized = normalizePositive(amount);
        StateChecks.isTrue(canBeCharged(normalized), "Payment mean cannot accept charge");
        accumulated = Money.calc(accumulated).add(normalized).toMoneyRoundedToCents();
    }

    /**
     * Decrements the accumulated amount of the payment mean by the given amount.
     * package-private Only called from Charge.rewind().
     * 
     * @param amount
     */
    /* package */ void rewind(Money amount) {
        Money normalized = normalizePositive(amount);
        StateChecks.isTrue(accumulated.greaterOrEqual(normalized),
            "Payment mean cannot be rewound with amount " + normalized);
        accumulated = Money.calc(accumulated).subtract(normalized).toMoneyRoundedToCents();
    }

    public Client getClient() {
        return client;
    }

    public Money getAccumulated() {
        return accumulated;
    }

    protected static void checkPositive(Money amount) {
        ArgumentChecks.isNotNull(amount, "Amount cannot be null");
        ArgumentChecks.isTrue(amount.greaterThan(Money.ZERO), "Amount must be positive");
    }

    protected static Money normalizePositive(Money amount) {
        ArgumentChecks.isNotNull(amount, "Amount cannot be null");
        Money result = Money.calc(amount).toMoneyRoundedToCents();
        checkPositive(result);
        return result;
    }

}
