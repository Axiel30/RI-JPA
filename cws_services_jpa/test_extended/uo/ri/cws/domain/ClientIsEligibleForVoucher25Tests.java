package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;
import uo.ri.util.RecommendationBuilder;

/**
 * Scenarios:
 * - A client with a paid invoice and three unused recommendations whose recommended clients have paid invoices is eligible
 * - A client with more than three valid recommendations is eligible
 * - A client without paid invoices is not eligible
 * - A client with fewer than three recommendations is not eligible
 * - Recommendations whose recommended clients have no paid invoices do not count
 * - Recommendations already used for a voucher do not count
 * - A client is eligible when at least three recommendations are valid among other invalid or used recommendations
 */
class ClientIsEligibleForVoucher25Tests {
	private Client client;

	@BeforeEach
	void setUp() {
		client = new ClientBuilder()
			.withPaidInvoice()
			.build();
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: exactly three unused recommendations
	 * AND: every recommended client has a paid invoice
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is eligible
	 */
	@Test
	void testEligibleWithExactlyThreeValidRecommendations() {
		addValidRecommendations(3);

		assertTrue(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: more than three unused recommendations
	 * AND: every recommended client has a paid invoice
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is eligible
	 */
	@Test
	void testEligibleWithMoreThanThreeValidRecommendations() {
		addValidRecommendations(4);

		assertTrue(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client without paid invoices
	 * AND: three unused recommendations whose recommended clients have paid invoices
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is not eligible
	 */
	@Test
	void testNotEligibleWithoutPaidInvoices() {
		client = new ClientBuilder()
			.withUnpaidInvoice()
			.build();
		addValidRecommendations(3);

		assertFalse(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: fewer than three unused recommendations whose recommended clients have paid invoices
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is not eligible
	 */
	@Test
	void testNotEligibleWithFewerThanThreeRecommendations() {
		addValidRecommendations(2);

		assertFalse(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: three unused recommendations
	 * AND: one of the recommended clients has no paid invoices
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is not eligible
	 */
	@Test
	void testRecommendationsWithoutPaidInvoicesDoNotCount() {
		addValidRecommendations(2);
		addRecommendationTo(new ClientBuilder().withUnpaidInvoice().build());

		assertFalse(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: three recommendations whose recommended clients have paid invoices
	 * AND: one of the recommendations has already been used for a voucher
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is not eligible
	 */
	@Test
	void testUsedRecommendationsDoNotCount() {
		addValidRecommendations(2);
		new RecommendationBuilder()
			.from(client)
			.to(new ClientBuilder().withPaidInvoice().build())
			.usedForVoucher()
			.build();

		assertFalse(client.isEligibleForVoucher25());
	}

	/**
	 * GIVEN: a client with a paid invoice
	 * AND: three unused recommendations whose recommended clients have paid invoices
	 * AND: other recommendations that are already used or whose recommended clients have no paid invoices
	 * WHEN: checking whether the client is eligible for a 25 euro voucher
	 * THEN: the client is eligible
	 */
	@Test
	void testEligibleWithThreeValidRecommendationsAmongInvalidOnes() {
		addValidRecommendations(3);
		addRecommendationTo(new ClientBuilder().withUnpaidInvoice().build());
		new RecommendationBuilder()
			.from(client)
			.to(new ClientBuilder().withPaidInvoice().build())
			.usedForVoucher()
			.build();

		assertTrue(client.isEligibleForVoucher25());
	}

	private void addValidRecommendations(int number) {
		for (int i = 0; i < number; i++) {
			addRecommendationTo(new ClientBuilder().withPaidInvoice().build());
		}
	}

	private void addRecommendationTo(Client recommended) {
		new RecommendationBuilder()
			.from(client)
			.to(recommended)
			.build();
	}
}
