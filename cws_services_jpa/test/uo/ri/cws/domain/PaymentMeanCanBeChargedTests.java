package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - Cash can always be charged a valid amount
 * - A valid credit card can be charged and an expired one cannot
 * - A voucher can be charged up to its available amount
 * - Null, zero and negative amounts are invalid for every payment mean
 */
class PaymentMeanCanBeChargedTests {

	private PaymentMean cash;
	private CreditCard card;
	private Voucher voucher;

	@BeforeEach
	void setUp() {
		Client client = new Client(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			);
		cash = new Cash(client);
		card = new CreditCard(
				new CreditCardNumber("4111111111111111"),
				"visa",
				YearMonth.now().plusMonths(1)
			);
		voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);
	}

	/**
	 * GIVEN: cash and a positive amount
	 * WHEN: asking whether it can be charged
	 * THEN: the answer is true
	 */
	@Test
	void testCashCanBeCharged() {
		assertTrue(cash.canBeCharged(Money.TEN));
	}

	/**
	 * GIVEN: a card whose validity is in the future
	 * WHEN: asking whether it can be charged
	 * THEN: the answer is true
	 */
	@Test
	void testValidCardCanBeCharged() {
		assertTrue(card.canBeCharged(Money.TEN));
	}

	/**
	 * GIVEN: an expired card
	 * WHEN: asking whether it can be charged
	 * THEN: the answer is false
	 */
	@Test
	void testExpiredCardCannotBeCharged() {
		CreditCard expired = new CreditCard(
				new CreditCardNumber("5555555555554444"),
				"mastercard",
				YearMonth.now().minusMonths(1)
			);

		assertFalse(expired.canBeCharged(Money.TEN));
	}

	/**
	 * GIVEN: a voucher and an amount equal to its available balance
	 * WHEN: asking whether it can be charged
	 * THEN: the answer is true
	 */
	@Test
	void testVoucherCanBeChargedUpToAvailableAmount() {
		assertTrue(voucher.canBeCharged(Money.of("100.00")));
	}

	/**
	 * GIVEN: a voucher and an amount greater than its available balance
	 * WHEN: asking whether it can be charged
	 * THEN: the answer is false
	 */
	@Test
	void testVoucherCannotBeChargedOverAvailableAmount() {
		assertFalse(voucher.canBeCharged(Money.of("100.01")));
	}

	/**
	 * GIVEN: any payment mean
	 * WHEN: asking whether it can be charged a null amount
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNullAmountIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> cash.canBeCharged(null));
		assertThrows(IllegalArgumentException.class, () -> card.canBeCharged(null));
		assertThrows(IllegalArgumentException.class, () -> voucher.canBeCharged(null));
	}

	/**
	 * GIVEN: any payment mean
	 * WHEN: asking whether it can be charged a zero amount
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testZeroAmountIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> cash.canBeCharged(Money.ZERO));
		assertThrows(IllegalArgumentException.class, () -> card.canBeCharged(Money.ZERO));
		assertThrows(IllegalArgumentException.class, () -> voucher.canBeCharged(Money.ZERO));
	}

	/**
	 * GIVEN: any payment mean
	 * WHEN: asking whether it can be charged a negative amount
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNegativeAmountIsRejected() {
		Money negative = Money.of("-0.01");

		assertThrows(IllegalArgumentException.class, () -> cash.canBeCharged(negative));
		assertThrows(IllegalArgumentException.class, () -> card.canBeCharged(negative));
		assertThrows(IllegalArgumentException.class, () -> voucher.canBeCharged(negative));
	}
}
