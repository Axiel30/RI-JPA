package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import uo.ri.util.RecommendationBuilder;

/**
 * Scenarios:
 * - Marking an unused recommendation for a voucher changes it to used
 */
class RecommendationMarkAsUsedForVoucherTests {
	/**
	 * GIVEN: an unused recommendation
	 * WHEN: marking the recommendation as used for a voucher
	 * THEN: the recommendation is marked as used
	 */
	@Test
	void testMarkRecommendationAsUsed() {
		Recommendation recommendation = new RecommendationBuilder().build();
		assertFalse(recommendation.isUsedForVoucher());

		recommendation.markAsUsedForVoucher();

		assertTrue(recommendation.isUsedForVoucher());
	}
}
