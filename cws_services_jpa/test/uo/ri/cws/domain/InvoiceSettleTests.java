package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.VoucherCode;

/**
 * Scenarios:
 * - Settling an invoice creates and links its charges and marks it as PAID
 * - An invoice can be settled with several payment means
 * - Charge amounts are normalized to cents before validation and storage
 * - Payment means belonging to a different client are accepted
 * - Settling rejects null or empty maps and null entries
 * - Settling rejects zero and negative amounts after normalization
 * - Settling rejects a sum different from the invoice total
 * - A zero-total invoice cannot be settled
 * - Settlement is atomic when any payment mean cannot be charged
 */
class InvoiceSettleTests {

	private static final Money WORK_ORDER_AMOUNT = Money.of("100.00");
	private static final Money INVOICE_TOTAL = Money.of("121.00");

	private Invoice invoice;
	private PaymentMean cash;

	@BeforeEach
	void setUp() {
		WorkOrder workOrder = DomainTestMocksFactory.oneApprovedWorkOrder(WORK_ORDER_AMOUNT);

		invoice = new Invoice(1L, List.of(workOrder));
		cash = new Cash(newClient("12345678Z"));
	}

	/**
	 * GIVEN: an issued invoice and a cash payment mean
	 * WHEN: the invoice is settled for its exact total
	 * THEN: the invoice is PAID
	 * AND: one charge is created with the right data
	 * AND: the payment mean accumulated is increased
	 */
	@Test
	void testSettleWithCash() {
		invoice.settle(Map.of(cash, INVOICE_TOTAL));

		assertTrue(invoice.isSettled());
		assertEquals(1, invoice.getCharges().size());
		Charge charge = invoice.getCharges().iterator().next();
		assertSame(invoice, charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
		assertEquals(INVOICE_TOTAL, charge.getAmount());
		assertEquals(INVOICE_TOTAL, cash.getAccumulated());
	}

	/**
	 * GIVEN: an issued invoice, cash and a voucher
	 * WHEN: the invoice total is split between both payment means
	 * THEN: two charges are created
	 * AND: each payment mean receives its corresponding amount
	 */
	@Test
	void testSettleWithSeveralPaymentMeans() {
		Voucher voucher = new Voucher(
				new VoucherCode("V-1"),
				Money.of("100.00")
			);

		invoice.settle(Map.of(
				cash, Money.of("71.00"),
				voucher, Money.of("50.00")
			));

		assertTrue(invoice.isSettled());
		assertEquals(2, invoice.getCharges().size());
		assertEquals(Money.of("71.00"), cash.getAccumulated());
		assertEquals(Money.of("50.00"), voucher.getAccumulated());
		assertEquals(Money.of("50.00"), voucher.getAvailableAmount());
	}

	/**
	 * GIVEN: two charge amounts with more than two decimals
	 * WHEN: each amount rounds with HALF_EVEN and their normalized sum equals the invoice total
	 * THEN: the invoice is settled with the normalized values
	 */
	@Test
	void testAmountsAreNormalizedBeforeAddingThem() {
		CreditCard card = newValidCard();

		invoice.settle(Map.of(
				cash, Money.of("60.005"),
				card, Money.of("61.005")
			));

		assertEquals(Money.of("60.00"), findChargeFor(cash).getAmount());
		assertEquals(Money.of("61.00"), findChargeFor(card).getAmount());
		assertEquals(INVOICE_TOTAL, Money.calc(cash.getAccumulated())
				.add(card.getAccumulated())
				.toMoneyExact());
	}

	/**
	 * GIVEN: a payment mean owned by a client unrelated to the invoice
	 * WHEN: it is used to settle the invoice
	 * THEN: the settlement succeeds
	 */
	@Test
	void testPaymentMeanFromAnyClientIsAccepted() {
		PaymentMean otherClientCash = new Cash(newClient("87654321X"));

		invoice.settle(Map.of(otherClientCash, INVOICE_TOTAL));

		assertTrue(invoice.isSettled());
		assertEquals(INVOICE_TOTAL, otherClientCash.getAccumulated());
	}

	/**
	 * GIVEN: an issued invoice
	 * WHEN: settling it with a null map
	 * THEN: an IllegalArgumentException is thrown and the invoice is unchanged
	 */
	@Test
	void testNullMapIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> invoice.settle(null));

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: an issued invoice
	 * WHEN: settling it with an empty map
	 * THEN: an IllegalArgumentException is thrown and the invoice is unchanged
	 */
	@Test
	void testEmptyMapIsRejected() {
		assertThrows(IllegalArgumentException.class, () -> invoice.settle(Map.of()));

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: a charge map with a null payment mean
	 * WHEN: settling the invoice
	 * THEN: an IllegalArgumentException is thrown and the invoice is unchanged
	 */
	@Test
	void testNullPaymentMeanIsRejected() {
		Map<PaymentMean, Money> charges = new HashMap<>();
		charges.put(null, INVOICE_TOTAL);

		assertThrows(IllegalArgumentException.class, () -> invoice.settle(charges));

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: a charge map with a null amount
	 * WHEN: settling the invoice
	 * THEN: an IllegalArgumentException is thrown and the invoice is unchanged
	 */
	@Test
	void testNullAmountIsRejected() {
		Map<PaymentMean, Money> charges = new HashMap<>();
		charges.put(cash, null);

		assertThrows(IllegalArgumentException.class, () -> invoice.settle(charges));

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: a charge amount that becomes zero when normalized to cents
	 * WHEN: settling the invoice
	 * THEN: an IllegalArgumentException is thrown and nothing is charged
	 */
	@Test
	void testZeroAmountAfterNormalizationIsRejected() {
		CreditCard card = newValidCard();
		Map<PaymentMean, Money> charges = new LinkedHashMap<>();
		charges.put(cash, Money.of("0.004"));
		charges.put(card, INVOICE_TOTAL);

		assertThrows(IllegalArgumentException.class, () -> invoice.settle(charges));

		assertInvoiceUnchanged();
		assertEquals(Money.ZERO, card.getAccumulated());
	}

	/**
	 * GIVEN: a negative charge amount
	 * WHEN: settling the invoice
	 * THEN: an IllegalArgumentException is thrown and nothing is charged
	 */
	@Test
	void testNegativeAmountIsRejected() {
		assertThrows(
				IllegalArgumentException.class,
				() -> invoice.settle(Map.of(cash, Money.of("-121.00")))
			);

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: charge amounts whose sum differs from the invoice total
	 * WHEN: settling the invoice
	 * THEN: an IllegalStateException is thrown and nothing is charged
	 */
	@Test
	void testAmountSumMustMatchInvoiceTotal() {
		assertThrows(
				IllegalStateException.class,
				() -> invoice.settle(Map.of(cash, Money.of("120.99")))
			);

		assertInvoiceUnchanged();
	}

	/**
	 * GIVEN: an issued invoice with total zero
	 * WHEN: trying to settle it with a positive charge
	 * THEN: an IllegalStateException is thrown and the invoice remains issued
	 */
	@Test
	void testZeroTotalInvoiceCannotBeSettled() {
		Invoice zeroInvoice = new Invoice(2L);

		assertThrows(
				IllegalStateException.class,
				() -> zeroInvoice.settle(Map.of(cash, Money.TEN))
			);

		assertFalse(zeroInvoice.isSettled());
		assertTrue(zeroInvoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, cash.getAccumulated());
	}

	/**
	 * GIVEN: several requested charges and one unchargeable payment mean
	 * WHEN: trying to settle the invoice
	 * THEN: the whole settlement is rejected before charging any payment mean
	 */
	@Test
	void testUnchargeablePaymentMeanRejectsWholeSettlement() {
		PaymentMean accepted = mock(PaymentMean.class);
		PaymentMean rejected = mock(PaymentMean.class);
		Money acceptedAmount = Money.of("50.00");
		Money rejectedAmount = Money.of("71.00");
		when(accepted.canBeCharged(acceptedAmount)).thenReturn(true);
		when(rejected.canBeCharged(rejectedAmount)).thenReturn(false);
		Map<PaymentMean, Money> charges = new LinkedHashMap<>();
		charges.put(accepted, acceptedAmount);
		charges.put(rejected, rejectedAmount);

		assertThrows(IllegalStateException.class, () -> invoice.settle(charges));

		assertInvoiceUnchanged();
		verify(accepted).canBeCharged(acceptedAmount);
		verify(rejected).canBeCharged(rejectedAmount);
		verify(accepted, never()).charge(acceptedAmount);
		verify(rejected, never()).charge(rejectedAmount);
	}

	private Charge findChargeFor(PaymentMean paymentMean) {
		return invoice.getCharges().stream()
				.filter(c -> c.getPaymentMean().equals(paymentMean))
				.findFirst()
				.orElseThrow();
	}

	private void assertInvoiceUnchanged() {
		assertFalse(invoice.isSettled());
		assertTrue(invoice.getCharges().isEmpty());
		assertEquals(Money.ZERO, cash.getAccumulated());
	}

	private CreditCard newValidCard() {
		return new CreditCard(
				new CreditCardNumber("4111111111111111"),
				"visa",
				YearMonth.now().plusMonths(1)
			);
	}

	private Client newClient(String nif) {
		return new Client(
				new Nif(nif),
				new FullName("Name", "Surname")
			);
	}
}
