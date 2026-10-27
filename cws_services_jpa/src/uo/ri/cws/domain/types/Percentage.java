package uo.ri.cws.domain.types;

import java.math.BigDecimal;

import uo.ri.util.assertion.ArgumentChecks;

public record Percentage(
		BigDecimal value
	) {
    
	private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public Percentage {
        value = normalize(value);
        ArgumentChecks.isTrue(isValid(value), "Percentage value is required and cannot be negative");
    }

    /**
     * Normalizes a raw percentage value by stripping trailing zeros.
     */
    private static BigDecimal normalize(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros();
    }

    /**
     * Pre-validates a raw percentage value before constructing the object.
     */
    public static boolean isValid(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    // ---- Factory methods ----
    /**
     * Creates a percentage from its percentage representation.
     * The supplied value is interpreted as a percentage, not as a ratio.
     * For example:
     *    - 21 represents 21%
     *    - 100 represents 100%
     *    - 150 represents 150%
     *
     * @param percent the percentage value
     * @return the corresponding percentage
     */
    public static Percentage ofPercent(BigDecimal percent) {
        return new Percentage(percent);
    }

    public static Percentage ofPercent(String percent) {
        return ofPercent(new BigDecimal(percent));
    }

    public static Percentage ofPercent(int percent) {
        return ofPercent(BigDecimal.valueOf(percent));
    }

    /**
     * Creates a percentage from its ratio representation.
     * For example, 0.21 corresponds to 21%.
     *
     * @param ratio the ratio equivalent to the percentage
     * @return the corresponding percentage
     */
    public static Percentage ofRatio(BigDecimal ratio) {
        return new Percentage(ratio.multiply(ONE_HUNDRED));
    }

    public static Percentage ofRatio(String ratio) {
        return ofRatio(new BigDecimal(ratio));
    }
    
    public BigDecimal asRatio() {
        return value.divide(ONE_HUNDRED);
    }

    public BigDecimal asMultiplier() {
        return asRatio().add(BigDecimal.ONE);
    }

    @Override
    public String toString() {
        return value + "%";
    }
}
