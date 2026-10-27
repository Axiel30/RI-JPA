package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;
import uo.ri.util.RecommendationBuilder;

/**
 * Scenarios:
 * - A client without recommendations has no recommendations available for a voucher
 * - Unused recommendations whose recommended clients have paid invoices are available
 * - Recommendations whose recommended clients have no paid invoices are not available
 * - Recommendations already used for a voucher are not available
 * - All valid recommendations are returned among other invalid or used recommendations
 */
class ClientGetRecommendationsAvailableForVoucher25Tests {
	private Client client;

	@BeforeEach
	void setUp() {
		client = new ClientBuilder().build();
	}

	/**
	 * GIVEN: a client without recommendations
	 * WHEN: querying its recommendations available for a 25 euro voucher
	 * THEN: an empty list is returned
	 */
	@Test
	void testNoRecommendations() {
		List<Recommendation> result = client.getRecommendationsAvailableForVoucher25();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a client with two unused recommendations
	 * AND: both recommended clients have paid invoices
	 * WHEN: querying the available recommendations
	 * THEN: both recommendations are returned
	 */
	@Test
	void testUnusedRecommendationsWithPaidInvoicesAreAvailable() {
		Recommendation first = addRecommendationTo(paidClient());
		Recommendation second = addRecommendationTo(paidClient());

		List<Recommendation> result = client.getRecommendationsAvailableForVoucher25();

		assertEquals(Set.of(first, second), Set.copyOf(result));
	}

	/**
	 * GIVEN: a recommendation whose recommended client has no paid invoices
	 * WHEN: querying the available recommendations
	 * THEN: the recommendation is not returned
	 */
	@Test
	void testRecommendationWithoutPaidInvoiceIsNotAvailable() {
		addRecommendationTo(new ClientBuilder().withUnpaidInvoice().build());

		List<Recommendation> result = client.getRecommendationsAvailableForVoucher25();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: a recommendation already used for a voucher
	 * AND: its recommended client has a paid invoice
	 * WHEN: querying the available recommendations
	 * THEN: the recommendation is not returned
	 */
	@Test
	void testUsedRecommendationIsNotAvailable() {
		new RecommendationBuilder()
			.from(client)
			.to( paidClient() )
			.usedForVoucher()
			.build();

		List<Recommendation> result = client.getRecommendationsAvailableForVoucher25();

		assertTrue(result.isEmpty());
	}

	/**
	 * GIVEN: valid, used and unpaid recommendations belonging to a client
	 * WHEN: querying the available recommendations
	 * THEN: all and only the valid recommendations are returned
	 */
	@Test
	void testOnlyValidRecommendationsAreReturned() {
		Recommendation first = addRecommendationTo(paidClient());
		Recommendation second = addRecommendationTo(paidClient());
		addRecommendationTo(new ClientBuilder().withUnpaidInvoice().build());
		new RecommendationBuilder()
			.from(client)
			.to(paidClient())
			.usedForVoucher()
			.build();

		List<Recommendation> result = client.getRecommendationsAvailableForVoucher25();

		assertEquals(Set.of(first, second), Set.copyOf(result));
	}

	private Recommendation addRecommendationTo(Client recommended) {
		return new RecommendationBuilder()
			.from(client)
			.to(recommended)
			.build();
	}

	private Client paidClient() {
		return new ClientBuilder().withPaidInvoice().build();
	}
}
