package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import org.apache.commons.lang3.RandomStringUtils;

public class CreditCardNumberGenerator {

	public static String generate() {
		String partial = "4" + RandomStringUtils.randomNumeric(14);
		return partial + calculateCheckDigit(partial);
	}

	private static int calculateCheckDigit(String partial) {
		int sum = 0;
		boolean doubleDigit = true;

		for (int i = partial.length() - 1; i >= 0; i--) {
			int digit = partial.charAt(i) - '0';
			if (doubleDigit) {
				digit *= 2;
				if (digit > 9) {
					digit -= 9;
				}
			}
			sum += digit;
			doubleDigit = !doubleDigit;
		}

		return (10 - sum % 10) % 10;
	}
}
