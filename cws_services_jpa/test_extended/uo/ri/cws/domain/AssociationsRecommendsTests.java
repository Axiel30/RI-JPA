package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Linking a recommender, a recommendation and a recommended client updates all association ends
 * - Unlinking a recommendation removes the link from all association ends
 * - The collection returned by getRecommendations() is a safe copy
 * - Linking a new recommendation adds it without removing existing recommendations
 * - Linking rejects a null recommender
 * - Linking rejects a null recommendation
 * - Linking rejects a null recommended client
 * - Linking rejects a recommendation that already has a recommender
 * - Linking rejects a recommendation that already has a recommended client
 * - Linking rejects a recommendation already present in the recommender's recommendations
 * - Linking rejects a recommended client that already has a recommendation
 * - Unlinking rejects a null recommendation
 * - Unlinking rejects a recommendation without a recommender
 * - Unlinking rejects a recommendation without a recommended client
 * - Unlinking rejects an inconsistent recommender association end
 * - Unlinking rejects an inconsistent recommended association end
 */
class AssociationsRecommendsTests {
	private Client recommender;
	private Client recommended;
	private Recommendation recommendation;

	@BeforeEach
	void setUp() {
		recommender = new Client(
			new Nif("12345678Z"),
			new FullName("Recommender", "Client")
		);
		recommended = new Client(
			new Nif("87654321X"),
			new FullName("Recommended", "Client")
		);
		recommendation = new Recommendation(recommender, recommended);
		Associations.Recommends.unlink(recommendation);
	}

	/**
	 * GIVEN: a recommender, a recommendation and a recommended client
	 * WHEN: Associations.Recommends.link is called
	 * THEN: the recommendation is in the recommender's recommendations
	 * AND: the recommendation refers to both clients
	 * AND: the recommended client refers to the recommendation
	 */
	@Test
	void testLinkOnRecommend() {
		Associations.Recommends.link(recommender, recommendation, recommended);

		assertTrue(recommender.getRecommendations().contains(recommendation));
		assertSame(recommender, recommendation.getRecommender());
		assertSame(recommended, recommendation.getRecommended());
		assertSame(recommendation, recommended.getRecommendation());
	}

	/**
	 * GIVEN: a linked recommendation
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: the recommendation is removed from the recommender
	 * AND: all single-valued association ends are null
	 */
	@Test
	void testUnlinkOnRecommend() {
		Associations.Recommends.link(recommender, recommendation, recommended);

		Associations.Recommends.unlink(recommendation);

		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommender with a linked recommendation
	 * WHEN: the returned recommendations set is modified
	 * THEN: the original association is not affected
	 */
	@Test
	void testSafeReturn() {
		Associations.Recommends.link(recommender, recommendation, recommended);

		Set<Recommendation> returned = recommender.getRecommendations();
		returned.remove(recommendation);

		assertTrue(returned.isEmpty());
		assertEquals(1, recommender.getRecommendations().size(), "It must be a copy of the collection");
	}

	/**
	 * GIVEN: a recommender with an existing recommendation
	 * WHEN: another recommendation is linked
	 * THEN: both recommendations belong to the recommender
	 */
	@Test
	void testLinkSeveralRecommendations() {
		Client anotherRecommended = new Client(
			new Nif("11111111H"),
			new FullName("Another", "Client")
		);
		Recommendation another = new Recommendation(recommender, anotherRecommended);
		Associations.Recommends.unlink(another);
		Associations.Recommends.link(recommender, recommendation, recommended);

		Associations.Recommends.link(recommender, another, anotherRecommended);

		assertEquals(2, recommender.getRecommendations().size());
		assertTrue(recommender.getRecommendations().contains(recommendation));
		assertTrue(recommender.getRecommendations().contains(another));
		assertSame(another, anotherRecommended.getRecommendation());
	}

	/**
	 * GIVEN: an unlinked recommendation and a recommended client
	 * WHEN: linking them with a null recommender
	 * THEN: an IllegalArgumentException is thrown
	 * AND: all association ends remain unlinked
	 */
	@Test
	void testLinkNullRecommender() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Recommends.link(null, recommendation, recommended)
		);

		assertUnlinked();
	}

	/**
	 * GIVEN: a recommender and a recommended client
	 * WHEN: linking them with a null recommendation
	 * THEN: an IllegalArgumentException is thrown
	 * AND: both clients remain unlinked
	 */
	@Test
	void testLinkNullRecommendation() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Recommends.link(recommender, null, recommended)
		);

		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: an unlinked recommender and recommendation
	 * WHEN: linking them with a null recommended client
	 * THEN: an IllegalArgumentException is thrown
	 * AND: their association ends remain unlinked
	 */
	@Test
	void testLinkNullRecommended() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Recommends.link(recommender, recommendation, null)
		);

		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
	}

	/**
	 * GIVEN: a recommendation that already has a recommender
	 * WHEN: linking it
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkRecommendationWithRecommender() {
		recommendation._setRecommender(recommender);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.link(recommender, recommendation, recommended)
		);

		assertSame(recommender, recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommendation that already has a recommended client
	 * WHEN: linking it
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkRecommendationWithRecommended() {
		recommendation._setRecommended(recommended);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.link(recommender, recommendation, recommended)
		);

		assertNull(recommendation.getRecommender());
		assertSame(recommended, recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommendation in a recommender's collection without its other ends
	 * WHEN: linking the association
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkRecommendationAlreadyInRecommender() {
		recommender._getRecommendations().add(recommendation);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.link(recommender, recommendation, recommended)
		);

		assertTrue(recommender.getRecommendations().contains(recommendation));
		assertNull(recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
		assertNull(recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommended client that already has another recommendation
	 * WHEN: linking a new recommendation
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkRecommendedWithRecommendation() {
		Client anotherRecommender = new Client(
			new Nif("11111111H"),
			new FullName("Another", "Recommender")
		);
		Recommendation existing = new Recommendation(anotherRecommender, recommended);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.link(recommender, recommendation, recommended)
		);

		assertSame(existing, recommended.getRecommendation());
		assertTrue(anotherRecommender.getRecommendations().contains(existing));
		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
	}

	/**
	 * GIVEN: no recommendation
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 */
	@Test
	void testUnlinkNullRecommendation() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Recommends.unlink(null)
		);
	}

	/**
	 * GIVEN: a recommendation without a recommender
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the recommendation remains unlinked
	 */
	@Test
	void testUnlinkRecommendationWithoutRecommender() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.unlink(recommendation)
		);

		assertUnlinked();
	}

	/**
	 * GIVEN: a recommendation with a recommender but without a recommended client
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkRecommendationWithoutRecommended() {
		recommendation._setRecommender(recommender);
		recommender._getRecommendations().add(recommendation);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.unlink(recommendation)
		);

		assertSame(recommender, recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().contains(recommendation));
	}

	/**
	 * GIVEN: a recommendation whose recommender does not contain it
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentRecommender() {
		recommendation._setRecommender(recommender);
		recommendation._setRecommended(recommended);
		recommended._setRecommendation(recommendation);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.unlink(recommendation)
		);

		assertSame(recommender, recommendation.getRecommender());
		assertSame(recommended, recommendation.getRecommended());
		assertFalse(recommender.getRecommendations().contains(recommendation));
		assertSame(recommendation, recommended.getRecommendation());
	}

	/**
	 * GIVEN: a recommended client that refers to a different recommendation
	 * WHEN: Associations.Recommends.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentRecommended() {
		Recommendation another = new Recommendation(recommender, recommended);
		Associations.Recommends.unlink(another);
		recommendation._setRecommender(recommender);
		recommendation._setRecommended(recommended);
		recommender._getRecommendations().add(recommendation);
		recommended._setRecommendation(another);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Recommends.unlink(recommendation)
		);

		assertSame(recommender, recommendation.getRecommender());
		assertSame(recommended, recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().contains(recommendation));
		assertSame(another, recommended.getRecommendation());
	}

	private void assertUnlinked() {
		assertNull(recommendation.getRecommender());
		assertNull(recommendation.getRecommended());
		assertTrue(recommender.getRecommendations().isEmpty());
		assertNull(recommended.getRecommendation());
	}
}
