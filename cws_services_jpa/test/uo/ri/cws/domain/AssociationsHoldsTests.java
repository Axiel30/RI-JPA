package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Creating a cash payment mean for a client links both sides of the association
 * - Unlinking a payment mean from a client removes the link on both sides
 * - The collection returned by getPaymentMeans() is a safe copy
 * - Linking a new payment mean adds it without removing the existing ones
 * - Linking rejects a null client
 * - Linking rejects a null payment mean
 * - Linking rejects a payment mean that already has a client
 * - Linking rejects a payment mean already present in the client's payment means
 * - Unlinking rejects a null payment mean
 * - Unlinking rejects a payment mean without a client
 * - Unlinking rejects an inconsistent association
 */
class AssociationsHoldsTests {
	private Client client;
	private PaymentMean cash;

	@BeforeEach
	void setUp() {
		client = new Client(new Nif("12345678Z"), new FullName("Name", "Surname"));
		cash = DomainTestMocksFactory.oneCash();
	}

	/**
	 * GIVEN: a client and a cash payment mean created for it (the constructor links them)
	 * WHEN: querying both objects
	 * THEN: the cash is in the client's payment means
	 * AND: the client is the cash's client
	 */
	@Test
	void testClientCashLinked() {
		Associations.Holds.link(client, cash);

		assertTrue(client.getPaymentMeans().contains(cash));
		assertSame(client, cash.getClient());
	}

	/**
	 * GIVEN: a client and a cash payment mean linked
	 * WHEN: Associations.Holds.unlink is called
	 * THEN: the cash is removed from the client's payment means
	 * AND: the cash's client is null
	 */
	@Test
	void testClientCashUnlinked() {
		Associations.Holds.link(client, cash);

		Associations.Holds.unlink(cash);

		assertFalse(client.getPaymentMeans().contains(cash));
		assertTrue(client.getPaymentMeans().isEmpty());
		assertNull(cash.getClient());
	}

	/**
	 * GIVEN: a client with a linked cash payment mean
	 * WHEN: the returned payment means set is modified
	 * THEN: the original association is not affected (it must be a copy)
	 */
	@Test
	void testSafeReturn() {
		Associations.Holds.link(client, cash);

		Set<PaymentMean> pms = client.getPaymentMeans();
		pms.remove(cash);

		assertTrue(pms.isEmpty());
		assertEquals(1, client.getPaymentMeans().size(), "It must be a copy of the collection");
	}

	/**
	 * GIVEN: a client already holding a cash payment mean AND a new credit card
	 * WHEN: Associations.Holds.link is called with the client and the credit card
	 * THEN: both payment means (cash and card) are in the client's payment means
	 * AND: the client is the credit card's client
	 */
	@Test
	void testHoldLink() {
		CreditCard cc = new CreditCard(new CreditCardNumber("4111111111111111"), "visa", YearMonth.now().plusYears(1));
		Associations.Holds.link(client, cash);
		Associations.Holds.link(client, cc);

		assertTrue(client.getPaymentMeans().contains(cash));
		assertTrue(client.getPaymentMeans().contains(cc));
		assertSame(client, cc.getClient());
	}

	/**
	 * GIVEN: a linked payment mean
	 * WHEN: linking it with a null client
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the payment mean and client remain unlinked
	 */
	@Test
	void testLinkNullClient() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Holds.link(null, cash)
		);

		assertNull(cash.getClient());
		assertTrue(client.getPaymentMeans().isEmpty());
	}

	/**
	 * GIVEN: a client
	 * WHEN: linking it with a null payment mean
	 * THEN: an IllegalArgumentException is thrown
	 * AND: its payment means remain unchanged
	 */
	@Test
	void testLinkNullPaymentMean() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Holds.link(client, null)
		);

		assertTrue(client.getPaymentMeans().isEmpty());
	}

	/**
	 * GIVEN: a payment mean already held by a client
	 * WHEN: linking it to another client
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkPaymentMeanWithClient() {
		Client another = new Client(
			new Nif("87654321X"),
			new FullName("Another", "Client")
		);
		Associations.Holds.link(client, cash);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Holds.link(another, cash)
		);

		assertSame(client, cash.getClient());
		assertTrue(client.getPaymentMeans().contains(cash));
		assertTrue(another.getPaymentMeans().isEmpty());
	}

	/**
	 * GIVEN: a payment mean present in a client but without its client end
	 * WHEN: linking both objects
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkPaymentMeanAlreadyInClient() {
		CreditCard card = new CreditCard(
			new CreditCardNumber("5555555555554444"),
			"mastercard",
			YearMonth.now().plusYears(1)
		);
		// Test-only backdoor: creates an inconsistent inverse association end.
		client._getPaymentMeans().add(card);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Holds.link(client, card)
		);

		assertNull(card.getClient());
		assertTrue(client.getPaymentMeans().contains(card));
	}

	/**
	 * GIVEN: no payment mean
	 * WHEN: Associations.Holds.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the payment mean and client remain unlinked
	 */
	@Test
	void testUnlinkNullPaymentMean() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Holds.unlink(null)
		);

		assertNull(cash.getClient());
		assertTrue(client.getPaymentMeans().isEmpty());
	}

	/**
	 * GIVEN: a payment mean without a client
	 * WHEN: Associations.Holds.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the payment mean remains unlinked
	 */
	@Test
	void testUnlinkPaymentMeanWithoutClient() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Holds.unlink(cash)
		);

		assertNull(cash.getClient());
		assertTrue(client.getPaymentMeans().isEmpty());
	}

	/**
	 * GIVEN: a payment mean whose client does not contain it
	 * WHEN: Associations.Holds.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentAssociation() {
		// Test-only backdoor: creates an inconsistent association end.
		cash._setClient(client);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Holds.unlink(cash)
		);

		assertSame(client, cash.getClient());
		assertFalse(client.getPaymentMeans().contains(cash));
	}

}
