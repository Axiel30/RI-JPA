package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.util.ClientBuilder;

/**
 * Scenarios:
 * - Creating a recommendation links the recommender and the recommended client and leaves it unused
 * - A recommender can make several recommendations
 * - Creating a recommendation with a null recommender is rejected
 * - Creating a recommendation with a null recommended client is rejected
 * - A client cannot recommend itself
 * - A client that has already been recommended cannot be recommended again
 */
class RecommendationConstructorTests {
	private Client recommender;
	private Client recommended;

	@BeforeEach
	void setUp() {
		recommender = new ClientBuilder().build();
		recommended = new ClientBuilder().build();
	}

	/**
	 * GIVEN: a recommender and a different client with no recommendation
	 * WHEN: a recommendation is created between them
	 * THEN: the recommendation refers to both clients
	 * AND: it is present in the recommender's recommendations
	 * AND: it is the recommendation of the recommended client
	 * AND: it has not been used for a voucher
	 */
	@Test
	void testCreateRecommendation() {
		Recommendation recommendation = new Recommendation(recommender, recommended);

		assertSame(recommender, recommendation.getRecommender());
		assertSame(recommended, recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().contains(recommendation));
		assertSame(recommendation, recommended.getRecommendation());
		assertFalse(recommendation.isUsedForVoucher());
	}

	/**
	 * GIVEN: a client that has already recommended another client
	 * WHEN: a second recommendation is created for a different client
	 * THEN: both recommendations belong to the recommender
	 */
	@Test
	void testRecommenderCanMakeSeveralRecommendations() {
		Recommendation first = new Recommendation(recommender, recommended);
		Recommendation second = new Recommendation(
			recommender,
			new ClientBuilder().build()
		);

		assertEquals(2, recommender.getRecommendations().size());
		assertTrue(recommender.getRecommendations().contains(first));
		assertTrue(recommender.getRecommendations().contains(second));
	}

	/**
	 * GIVEN: a client with no recommendation
	 * WHEN: a recommendation is created with a null recommender
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the client remains without a recommendation
	 */
	@Test
	void testNullRecommenderIsRejected() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Recommendation(null, recommended)
		);

		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommender without recommendations
	 * WHEN: a recommendation is created with a null recommended client
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the recommender remains without recommendations
	 */
	@Test
	void testNullRecommendedIsRejected() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Recommendation(recommender, null)
		);

		assertTrue(recommender.getRecommendations().isEmpty());
	}

	/**
	 * GIVEN: a client
	 * WHEN: a recommendation is created with that client at both ends
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the client remains outside the recommendation association
	 */
	@Test
	void testClientCannotRecommendItself() {
		assertThrows(
			IllegalArgumentException.class,
			() -> new Recommendation(recommender, recommender)
		);

		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommender.getRecommendation());
	}

	/**
	 * GIVEN: a client that has already been recommended
	 * WHEN: another recommendation is created for that client
	 * THEN: an IllegalStateException is thrown
	 * AND: the existing recommendation remains unchanged
	 * AND: the new recommender remains without recommendations
	 */
	@Test
	void testRecommendedClientCannotBeRecommendedAgain() {
		Client originalRecommender = new ClientBuilder().build();
		Recommendation original = new Recommendation(
			originalRecommender,
			recommended
		);

		assertThrows(
			IllegalStateException.class,
			() -> new Recommendation(recommender, recommended)
		);

		assertSame(original, recommended.getRecommendation());
		assertTrue(originalRecommender.getRecommendations().contains(original));
		assertTrue(recommender.getRecommendations().isEmpty());
	}
}
