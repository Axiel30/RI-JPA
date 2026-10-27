package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
 * - A charge increases the accumulated amount of its payment mean
 * - A voucher charge also decreases its available amount
 * - A charge links with its invoice and payment mean
 * - Charge amounts are normalized to cents using HALF_EVEN
 * - An expired card and a voucher without funds cannot be charged
 * - Null invoice, payment mean or amount arguments are rejected
 * - Zero and negative amounts are rejected after normalization
 */
class ChargeConstructorTests {

	private static final Money AMOUNT = Money.of("100.00");

	private Invoice invoice;
	private PaymentMean cash;

	@BeforeEach
	void setUp() {
		invoice = new Invoice(123L);
		cash = new Cash(new Client(
				new Nif("12345678Z"),
				new FullName("Name", "Surname")
			));
	}

	/**
	 * GIVEN: an invoice and a cash payment mean
	 * WHEN: a charge is created
	 * THEN: its fields are initialized
	 * AND: both internal association ends are linked
	 * AND: the payment mean accumulated is increased
	 */
	@Test
	void testCashCharge() {
		Charge charge = new Charge(invoice, cash, AMOUNT);

		assertSame(invoice, charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
		assertEquals(AMOUNT, charge.getAmount());
		assertTrue(invoice.getCharges().contains(charge));
		assertEquals(AMOUNT, cash.getAccumulated());
	}

	/**
	 * GIVEN: a voucher with enough available funds
	 * WHEN: a charge is created against it
	 * THEN: accumulated is increased
	 * AND: available is decreased by the same amount
	 */
	@Test
	void testVoucherCharge() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("150.00")
			);

		new Charge(invoice, voucher, AMOUNT);

		assertEquals(AMOUNT, voucher.getAccumulated());
		assertEquals(Money.of("50.00"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: an amount with more than two decimals
	 * WHEN: a charge is created
	 * THEN: the stored and accumulated amounts are rounded with HALF_EVEN
	 */
	@Test
	void testAmountIsRoundedToCentsUsingHalfEven() {
		Charge charge = new Charge(invoice, cash, Money.of("10.005"));

		assertEquals(Money.of("10.00"), charge.getAmount());
		assertEquals(Money.of("10.00"), cash.getAccumulated());
	}

	/**
	 * GIVEN: an expired credit card
	 * WHEN: trying to create a charge against it
	 * THEN: an IllegalStateException is thrown without linking or charging it
	 */
	@Test
	void testExpiredCardCannotBeCharged() {
		CreditCard expired = new CreditCard(
				new CreditCardNumber("4111111111111111"),
				"visa",
				YearMonth.now().minusMonths(1)
			);

		assertThrows(
				IllegalStateException.class,
				() -> new Charge(invoice, expired, AMOUNT)
			);

		assertTrue(invoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, expired.getAccumulated());
	}

	/**
	 * GIVEN: a voucher without enough available funds
	 * WHEN: trying to create a charge against it
	 * THEN: an IllegalStateException is thrown without linking or charging it
	 */
	@Test
	void testVoucherWithoutFundsCannotBeCharged() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-2"),
				Money.of("99.99")
			);

		assertThrows(
				IllegalStateException.class,
				() -> new Charge(invoice, voucher, AMOUNT)
			);

		assertTrue(invoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, voucher.getAccumulated());
		assertEquals(Money.of("99.99"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: the arguments needed to create a charge
	 * WHEN: invoice, payment mean or amount is null
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNullArgumentsAreRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(null, cash, AMOUNT)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, null, AMOUNT)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, cash, null)
			);
	}

	/**
	 * GIVEN: a zero, negative or sub-cent amount that normalizes to zero
	 * WHEN: trying to create a charge
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testNonPositiveAmountsAreRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, cash, Money.ZERO)
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, cash, Money.of("-0.01"))
			);
		assertThrows(
				IllegalArgumentException.class,
				() -> new Charge(invoice, cash, Money.of("0.004"))
			);
	}
}
