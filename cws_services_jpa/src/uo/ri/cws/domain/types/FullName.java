package uo.ri.cws.domain.types;

import java.text.Normalizer;

import uo.ri.util.assertion.ArgumentChecks;

public record FullName ( 
		String name,
		String surname
	){

    public FullName {
        name = normalize(name);
        surname = normalize(surname);

        ArgumentChecks.isTrue(isValid(name, surname), "Name and surname cannot be empty");
   }

    /**
     * Normalizes a raw name/surname: trims, collapses inner whitespace
     * and applies Unicode NFC normalization.
     */
    private static String normalize(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim().replaceAll("\\s+", " ");
        return Normalizer.normalize(value, Normalizer.Form.NFC);
    }

    /**
     * Pre-validates the raw name and surname before constructing the object.
     */
    public static boolean isValid(String name, String surname) {
        return isNotBlank(name) && isNotBlank(surname);
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.isBlank();
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

}