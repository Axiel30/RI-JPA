package uo.ri.cws.domain.types;

import java.util.regex.Pattern;

import uo.ri.util.assertion.ArgumentChecks;

public record PhoneNumber(
		String phone
	) {

    // E.164: + followed by 7 to 15 digits (country code + subscriber number)
    private static final Pattern PATTERN = Pattern.compile("^\\+[1-9]\\d{6,14}$");

    public PhoneNumber {
        phone = normalize(phone);

        ArgumentChecks.isTrue(isValid(phone), "Invalid phone: " + phone);
    }

    /**
     * Normalizes a raw phone number to E.164-like digits (converting a leading
     * "00" international prefix to "+" and stripping any non-digit character).
     */
    private static String normalize(String v) {
        if (v == null) {
            return null;
        }
        String s = v.trim();
        if (s.startsWith("00")) {           // 0034... -> +34...
            s = "+" + s.substring(2);
        }
        if (s.startsWith("+")) {
            return "+" + s.substring(1).replaceAll("\\D", "");
        }
        return s.replaceAll("\\D", "");     // if no prefix will fail validation
    }

    /**
     * Pre-validates a raw phone number before constructing the object.
     */
    public static boolean isValid(String phone) {
        if (phone == null) {
            return false;
        }
        return PATTERN.matcher(normalize(phone)).matches();
    }

}
