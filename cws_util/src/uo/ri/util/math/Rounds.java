package uo.ri.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Rounds {

	/**
	 * Rounds to 1 decimal place (tenths)
	 */
	public static double toTenths(double value) {
		return round(value, 1);
	}
	
	/**
	 * Rounds to 2 decimal places (cent units)
	 */
	public static double toCents(double value) {
		return round(value, 2);
	}

	/**
	 * Rounds to 3 decimal places (milli units)
	 */
	public static double toMilis(double value) {
		return round(value, 3);
	}
	
	private static double round(double value, int decimalPlaces) {
        return BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.HALF_UP)
                .doubleValue();
    }
	
}
