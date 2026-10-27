package uo.ri.cws.domain.types;

import java.util.regex.Pattern;

import uo.ri.util.assertion.ArgumentChecks;

public record LicensePlate(
		String licensePlate
	) {

    private static final Pattern PATTERN = Pattern.compile("^\\d{4}[BCDFGHJKLMNPRSTVWXYZ]{3}$");

    public LicensePlate {
        licensePlate = normalize(licensePlate);
    	ArgumentChecks.isTrue(isValid(licensePlate), "Invalid plate");
    }

    /**
     * Normalizes a raw license plate: trims, removes internal spaces
     * and dashes, and upper-cases it.
     */
    private static String normalize(String licensePlate) {
        if (licensePlate == null) {
            return null;
        }
        return licensePlate.trim()
                .replaceAll("\\s+", "")
                .replace("-", "")
                .toUpperCase();
    }

    /**
     * Pre-validates a raw license plate before constructing the object.
     */
    public static boolean isValid(String licensePlate) {
        if (licensePlate == null || licensePlate.isBlank()) {
            return false;
        }
        return PATTERN.matcher(normalize(licensePlate)).matches();
    }
    
    public LicensePlate(String numbers, String letters) {
    	this(numbers + letters);
   }
    
    public LicensePlate(int numbers, String letters) {
        this(String.format("%04d", numbers) + letters);
    }
    
    @Override
    public String toString() {
        return licensePlate.substring(0, 4) + " " + licensePlate.substring(4);
    }

}
