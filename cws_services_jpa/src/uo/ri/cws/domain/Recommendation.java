package uo.ri.cws.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TRECOMMENDATIONS", uniqueConstraints = { @UniqueConstraint(columnNames = { "RECOMMENDER_ID", "RECOMMENDED_ID" }) })
public class Recommendation extends BaseEntity {

	// Natural attributes
	@Column(name = "USEDFORVOUCHER")
	private boolean usedForVoucher = false;

	// Accidental attributes
	@ManyToOne
	@JoinColumn(name = "RECOMMENDER_ID")
	private Client recommender;

	@OneToOne
	@JoinColumn(name = "RECOMMENDED_ID", unique = true)
	private Client recommended;

	// ---- Constructors ----
	protected Recommendation() {}

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
	public String toString() {
		return "Recommendation [recommender=" + recommender + ", recommended=" + recommended + ", usedForVoucher=" + usedForVoucher + "]";
	}

}
