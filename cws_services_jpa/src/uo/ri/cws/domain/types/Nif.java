package uo.ri.cws.domain.types;


import java.util.regex.Pattern;

import uo.ri.util.assertion.ArgumentChecks;

public record Nif(
		String nif
	) {

    private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final Pattern PATTERN = Pattern.compile("^[0-9XYZ][0-9]{7}[A-Z]$");

    public Nif {
        nif = normalize(nif);
        ArgumentChecks.isTrue(isValid(nif), "Invalid NIF " + nif);
    }

    /**
     * Normalizes a raw NIF/NIE: trims, removes spaces and upper-cases it.
     */
    private static String normalize(String nif) {
        return nif == null ? null : nif.trim().replace(" ", "").toUpperCase();
    }

    /**
     * Pre-validates a raw NIF/NIE (format and checksum) before constructing the object.
     */
    public static boolean isValid(String nif) {
        if (nif == null) {
            return false;
        }
        String normalized = normalize(nif);
        return PATTERN.matcher(normalized).matches() && isValidChecksum(normalized);
    }

    private static boolean isValidChecksum(String nif) {
        char first = nif.charAt(0);
        char control = nif.charAt(8);

        String numberPart = switch (first) {
            case 'X' -> "0" + nif.substring(1, 8);
            case 'Y' -> "1" + nif.substring(1, 8);
            case 'Z' -> "2" + nif.substring(1, 8);
            default -> nif.substring(0, 8);
        };

        long number = Long.parseLong(numberPart);
        char expected = LETTERS.charAt((int) (number % 23));

        return expected == control;
    }

    public boolean isNie() {
        char c = nif.charAt(0);
        return c == 'X' || c == 'Y' || c == 'Z';
    }

}
