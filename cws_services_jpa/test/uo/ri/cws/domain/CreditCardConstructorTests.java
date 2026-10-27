package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.CreditCardNumber;

/**
 * Scenarios:
 * - A credit card is created with the given number, type and valid thru date
 * - Credit card number cannot be null
 * - Credit card type cannot be null
 * - Credit card valid thru date cannot be null
 */
class CreditCardConstructorTests {

	private static final CreditCardNumber NUMBER = new CreditCardNumber("4111111111111111");
	private static final String TYPE = "visa";
	private static final YearMonth VALID_THRU = YearMonth.now().plusMonths( 2 );

	/**
	 * GIVEN: a valid number, type and valid thru date
	 * WHEN: we create a credit card with those values
	 * THEN: the credit card is created with that number and type
	 */
	@Test
	void testConstructorWithValidValues() {
		CreditCard creditCard = new CreditCard(NUMBER, TYPE, VALID_THRU);

		assertEquals(NUMBER, creditCard.getNumber());
		assertEquals(TYPE, creditCard.getType());
		assertEquals(VALID_THRU, creditCard.getValidThru());
	}

	/**
	 * WHEN: we create a credit card with a null number
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNumberCannotBeNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new CreditCard(null, TYPE, VALID_THRU)
			);
	}

	/**
	 * WHEN: we create a credit card with a null type
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testTypeCannotBeNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new CreditCard(NUMBER, null, VALID_THRU)
			);
	}

	/**
	 * WHEN: we create a credit card with a null valid thru date
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testValidThruCannotBeNull() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new CreditCard(NUMBER, TYPE, null)
			);
	}

}
