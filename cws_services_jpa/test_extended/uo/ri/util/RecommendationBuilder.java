package uo.ri.util;

import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Recommendation;

public class RecommendationBuilder implements Builder<Recommendation> {
	private Client recommender = new ClientBuilder().build();
	private Client recommended = new ClientBuilder().build();
	private boolean usedForVoucher;

	@Override
	public Recommendation build() {
		Recommendation recommendation = new Recommendation(recommender, recommended);
		if (usedForVoucher) {
			recommendation.markAsUsedForVoucher();
		}
		return recommendation;
	}

	public RecommendationBuilder from(Client recommender) {
		this.recommender = recommender;
		return this;
	}

	public RecommendationBuilder to(Client recommended) {
		this.recommended = recommended;
		return this;
	}

	public RecommendationBuilder usedForVoucher() {
		this.usedForVoucher = true;
		return this;
	}
}
