package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;
import uo.ri.util.RecommendationBuilder;

/**
 * Scenarios:
 * - Exactly three available recommendations are marked as used
 * - Only three recommendations are marked when more than three are available
 * - Used or unavailable recommendations are ignored when choosing the three recommendations
 * - Trying to mark recommendations when fewer than three are available is rejected without changing any recommendation
 */
class ClientMarkThreeRecommendationsAsUsedForVoucher25Tests {
	private Client client;

	@BeforeEach
	void setUp() {
		client = new ClientBuilder().withPaidInvoice().build();
	}

	/**
	 * GIVEN: a client with exactly three available recommendations
	 * WHEN: marking three recommendations as used for a voucher
	 * THEN: all three recommendations are marked as used
	 */
	@Test
	void testMarkExactlyThreeRecommendations() {
		List<Recommendation> recommendations = addValidRecommendations(3);

		client.markThreeRecommendationsAsUsedForVoucher25();

		assertTrue(recommendations.stream()
			.allMatch(Recommendation::isUsedForVoucher));
	}

	/**
	 * GIVEN: a client with four available recommendations
	 * WHEN: marking three recommendations as used for a voucher
	 * THEN: exactly three recommendations are marked as used
	 * AND: one recommendation remains unused
	 */
	@Test
	void testOnlyThreeRecommendationsAreMarked() {
		List<Recommendation> recommendations = addValidRecommendations(4);

		client.markThreeRecommendationsAsUsedForVoucher25();

		assertEquals(3, recommendations.stream()
			.filter(Recommendation::isUsedForVoucher)
			.count());
		assertEquals(1, recommendations.stream()
			.filter(recommendation -> !recommendation.isUsedForVoucher())
			.count());
	}

	/**
	 * GIVEN: three available recommendations
	 * AND: one used recommendation and one recommendation without a paid invoice
	 * WHEN: marking three recommendations as used for a voucher
	 * THEN: the three available recommendations are marked
	 * AND: the used recommendation remains used
	 * AND: the unavailable recommendation remains unused
	 */
	@Test
	void testUsedAndUnavailableRecommendationsAreIgnored() {
		List<Recommendation> available = addValidRecommendations(3);
		Recommendation alreadyUsed = new RecommendationBuilder()
			.from(client)
			.to(paidClient())
			.usedForVoucher()
			.build();
		Recommendation unavailable = addRecommendationTo(
			new ClientBuilder().withUnpaidInvoice().build()
		);

		client.markThreeRecommendationsAsUsedForVoucher25();

		assertTrue(available.stream()
			.allMatch(Recommendation::isUsedForVoucher));
		assertTrue(alreadyUsed.isUsedForVoucher());
		assertFalse(unavailable.isUsedForVoucher());
	}

	/**
	 * GIVEN: a client with only two available recommendations
	 * WHEN: trying to mark three recommendations as used for a voucher
	 * THEN: an IllegalStateException is thrown
	 * AND: both recommendations remain unused
	 */
	@Test
	void testFewerThanThreeRecommendationsAreRejected() {
		List<Recommendation> recommendations = addValidRecommendations(2);

		assertThrows(
			IllegalStateException.class,
			client::markThreeRecommendationsAsUsedForVoucher25
		);

		assertTrue(recommendations.stream()
			.noneMatch(Recommendation::isUsedForVoucher));
	}

	private List<Recommendation> addValidRecommendations(int number) {
		List<Recommendation> result = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			result.add(addRecommendationTo(paidClient()));
		}
		return result;
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
