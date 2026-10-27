package uo.ri.cws.domain;

import java.util.Objects;

import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Recommendation {

	// Natural attributes
	private boolean usedForVoucher = false;

	// Accidental attributes
	private Client recommender;
	private Client recommended;

	// ---- Constructors ----
	public Recommendation(Client recommender, Client recommended) {
		ArgumentChecks.isNotNull(recommender, "Recommender must not be null");
		ArgumentChecks.isNotNull(recommended, "Recommended client must not be null");
		ArgumentChecks.isFalse(recommender.equals(recommended), "Client cannot recommend itself");
		StateChecks.isTrue(recommended.getRecommendation() == null, "Recommended client already has a recommendation");

		Associations.Recommends.link(recommender, this, recommended);
	}

	// ---- Getters ----
	public Client getRecommender() {
		return recommender;
	}

	public Client getRecommended() {
		return recommended;
	}

	public boolean isUsedForVoucher() {
		return usedForVoucher;
	}

	// ---- Business methods ----
	public void markAsUsedForVoucher() {
		this.usedForVoucher = true;
	}

	// ---- Association helpers ----
	void _setRecommender(Client recommender) {
		this.recommender = recommender;
	}

	void _setRecommended(Client recommended) {
		this.recommended = recommended;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Recommendation other = (Recommendation) obj;
		return Objects.equals(recommender, other.recommender) && Objects.equals(recommended, other.recommended);
	}

	@Override
	public int hashCode() {
		return Objects.hash(recommender, recommended);
	}

	@Override
	public String toString() {
		return "Recommendation [recommender=" + recommender + ", recommended=" + recommended + ", usedForVoucher=" + usedForVoucher + "]";
	}

}
