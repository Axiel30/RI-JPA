package uo.ri.cws.domain.types;

import java.util.Objects;

import uo.ri.util.assertion.ArgumentChecks;

public record CreditCardNumber(String number) {

    public CreditCardNumber {
        number = normalize(number);
    	ArgumentChecks.isTrue(isValid(number), "Invalid credit card");
    }

    /**
     * Normalizes a raw credit card number by removing every non-digit character.
     */
    private static String normalize(String input) {
        return input == null ? null : input.replaceAll("\\D", "");
    }

    /**
     * Pre-validates a raw credit card number before constructing the object.
     */
    public static boolean isValid(String number) {
        if (number == null) {
            return false;
        }
        String normalized = normalize(number);
        return normalized.length() >= 13 && normalized.length() <= 19 && isValidLuhn(normalized);
    }

    private static boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alt = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';

            if (alt) {
                n *= 2;
                if (n > 9) {
					n -= 9;
				}
            }

            sum += n;
            alt = !alt;
        }

        return sum % 10 == 0;
    }

    public String last4() {
        return number.substring(number.length() - 4);
    }

    @Override
    public String toString() {
        return "**** **** **** " + last4();
    }

    public String number() {
    	return number;
    }
    
    @Override
    public boolean equals(Object o) {
        return o instanceof CreditCardNumber c && number.equals(c.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
