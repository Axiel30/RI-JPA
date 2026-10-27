package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Scenarios:
 * - Creating a charge atomically links it with its invoice and payment mean
 * - Atomically unlinking a charge removes all its association ends
 * - The collection returned by Invoice.getCharges() is a safe copy
 * - Atomic linking rejects null arguments
 * - Atomic linking rejects a charge that already has an invoice
 * - Atomic linking rejects a charge that already has a payment mean
 * - Atomic linking rejects a charge already present in the invoice
 * - Atomic unlinking rejects a null charge
 * - Atomic unlinking rejects a charge without an invoice
 * - Atomic unlinking rejects a charge without a payment mean
 * - Atomic unlinking rejects an inconsistent inverse end
 */
class AssociationsSettlesWithdrawsTests {

	private Invoice invoice;
	private PaymentMean cash;
	private Charge charge;

	@BeforeEach
	void setUp() {
		invoice = new Invoice(1L);
		cash = DomainTestMocksFactory.oneCash();
		charge = DomainTestMocksFactory.oneCharge();
	}

	/**
	 * GIVEN: an unlinked invoice, charge and payment mean
	 * WHEN: the charge is atomically linked
	 * THEN: it is added to the invoice
	 * AND: both ends are set on the charge
	 */
	@Test
	void testChargeAtomicallyLinked() {
		Associations.SettlesWithdraws.atomicLink(invoice, charge, cash);

		assertTrue(invoice.getCharges().contains(charge));
		assertSame(invoice, charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
	}

	/**
	 * GIVEN: a charge linked to an invoice and a payment mean
	 * WHEN: the charge is atomically unlinked
	 * THEN: it is removed from the invoice
	 * AND: both ends are cleared on the charge
	 */
	@Test
	void testChargeAtomicallyUnlinked() {
		Associations.SettlesWithdraws.atomicLink(invoice, charge, cash);

		Associations.SettlesWithdraws.atomicUnlink(charge);

		assertFalse(invoice.getCharges().contains(charge));
		assertTrue(invoice.getCharges().isEmpty());
		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
	}

	/**
	 * GIVEN: an invoice with a linked charge
	 * WHEN: the charge is removed from the returned collection
	 * THEN: the invoice's internal collection remains unchanged
	 */
	@Test
	void testSafeReturnOnInvoice() {
		Associations.SettlesWithdraws.atomicLink(invoice, charge, cash);

		Set<Charge> charges = invoice.getCharges();
		charges.remove(charge);

		assertTrue(charges.isEmpty());
		assertTrue(invoice.getCharges().contains(charge));
	}

	/**
	 * GIVEN: unlinked association ends
	 * WHEN: atomicLink is called with each null argument
	 * THEN: an IllegalArgumentException is thrown every time
	 * AND: the association remains unchanged
	 */
	@Test
	void testAtomicLinkNullArguments() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.SettlesWithdraws.atomicLink(null, charge, cash)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.SettlesWithdraws.atomicLink(invoice, null, cash)
		);
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.SettlesWithdraws.atomicLink(invoice, charge, null)
		);

		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
		assertFalse(invoice.getCharges().contains(charge));
	}

	/**
	 * GIVEN: a charge already linked to an invoice and payment mean
	 * WHEN: linking it to another invoice
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testAtomicLinkChargeWithInvoice() {
		Invoice another = new Invoice(2L);
		Associations.SettlesWithdraws.atomicLink(invoice, charge, cash);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicLink(another, charge, cash)
		);

		assertSame(invoice, charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
		assertTrue(invoice.getCharges().contains(charge));
		assertTrue(another.getCharges().isEmpty());
	}

	/**
	 * GIVEN: a charge whose payment-mean end is already set
	 * WHEN: atomically linking the charge
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkChargeWithPaymentMean() {
		// Test-only backdoor: creates an inconsistent association end.
		charge._setPaymentMean(cash);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicLink(invoice, charge, cash)
		);

		assertNull(charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
		assertFalse(invoice.getCharges().contains(charge));
	}

	/**
	 * GIVEN: a charge present in an invoice without its own ends set
	 * WHEN: atomically linking the charge
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicLinkChargeAlreadyInInvoice() {
		// Test-only backdoor: creates an inconsistent inverse association end.
		invoice._getCharges().add(charge);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicLink(invoice, charge, cash)
		);

		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
		assertTrue(invoice.getCharges().contains(charge));
	}

	/**
	 * GIVEN: no charge
	 * WHEN: atomicUnlink is called with null
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the association remains empty
	 */
	@Test
	void testAtomicUnlinkNullCharge() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.SettlesWithdraws.atomicUnlink(null)
		);

		assertNull(charge.getInvoice());
		assertNull(charge.getPaymentMean());
		assertTrue(invoice.getCharges().isEmpty());
	}

	/**
	 * GIVEN: a charge with a payment mean but no invoice
	 * WHEN: atomically unlinking the charge
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkChargeWithoutInvoice() {
		// Test-only backdoor: creates an inconsistent association end.
		charge._setPaymentMean(cash);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicUnlink(charge)
		);

		assertNull(charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
	}

	/**
	 * GIVEN: a charge linked to an invoice but without a payment mean
	 * WHEN: atomically unlinking the charge
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkChargeWithoutPaymentMean() {
		// Test-only backdoors: create an inconsistent association deliberately.
		charge._setInvoice(invoice);
		invoice._getCharges().add(charge);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicUnlink(charge)
		);

		assertSame(invoice, charge.getInvoice());
		assertNull(charge.getPaymentMean());
		assertTrue(invoice.getCharges().contains(charge));
	}

	/**
	 * GIVEN: a charge whose ends are set but which is absent from the invoice
	 * WHEN: atomically unlinking the charge
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association remains unchanged
	 */
	@Test
	void testAtomicUnlinkInconsistentInvoice() {
		// Test-only backdoors: create an inconsistent association deliberately.
		charge._setInvoice(invoice);
		charge._setPaymentMean(cash);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.SettlesWithdraws.atomicUnlink(charge)
		);

		assertSame(invoice, charge.getInvoice());
		assertSame(cash, charge.getPaymentMean());
		assertFalse(invoice.getCharges().contains(charge));
	}
}
