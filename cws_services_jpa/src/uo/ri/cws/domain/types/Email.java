package uo.ri.cws.domain.types;

import java.util.regex.Pattern;

import uo.ri.util.assertion.ArgumentChecks;

public record Email(String email) {

    private static final Pattern SIMPLE_EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public Email {
        email = normalize(email);
    	ArgumentChecks.isTrue(isValid(email), "Invalid email format");
    }

    /**
     * Normalizes a raw email address: trims and lower-cases it.
     */
    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase();
    }

    /**
     * Pre-validates a raw email address before constructing the object.
     */
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        return SIMPLE_EMAIL_PATTERN.matcher(normalize(email)).matches();
    }

    // ---- Behavior ----
    public String localPart() {
        return email.substring(0, email.indexOf('@'));
    }

    public String domain() {
        return email.substring(email.indexOf('@') + 1);
    }

    public boolean isFromDomain(String domain) {
        if (domain == null || domain.isBlank()) {
            return false;
        }
        return domain().equalsIgnoreCase(domain.trim());
    }

    public boolean containsDomain(String fragment) {
        if (fragment == null || fragment.isBlank()) {
            return false;
        }
        return domain().toLowerCase().contains(fragment.toLowerCase());
    }

}
