package uo.ri.cws.domain.types;

import uo.ri.util.assertion.ArgumentChecks;

public record Address(
	    String street,
		String city,
		String country,
		String postalCode
	) {

	public Address {
		street = normalize(street);
		city = normalize(city);
		country = normalize(country);
		postalCode = normalize(postalCode);

		ArgumentChecks.isTrue(isValid(street, city, country, postalCode),
				"Street, city, country and postal code are required");
	}

	/**
	 * Normalizes a raw text field: trims and collapses inner whitespace.
	 */
	private static String normalize(String v) {
		return v == null ? null : v.trim().replaceAll("\\s+", " ");
	}

	/**
	 * Pre-validates the raw fields before constructing the object.
	 */
	public static boolean isValid(String street, String city, String country, String postalCode) {
		return isNotBlank(street) && isNotBlank(city) && isNotBlank(country) && isNotBlank(postalCode);
	}

	private static boolean isNotBlank(String s) {
		return s != null && !s.isBlank();
	}

}