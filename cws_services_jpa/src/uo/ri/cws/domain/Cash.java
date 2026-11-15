package uo.ri.cws.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TCASHES")
@DiscriminatorValue("Cash")
public class Cash extends PaymentMean {

	// ---- Constructors ----
	protected Cash() {}

	public Cash(Client client) {
		ArgumentChecks.isNotNull(client, "Client must not be null");
		this._setClient(client);
		// Add to client's payment means collection if exists; will be handled by Associations.Holds.link elsewhere
	}

	// ---- Business methods ----
	@Override
	public boolean canBeCharged(Money amount) {
		checkPositive(amount);
		return true;
	}

	// ---- toString ----
	@Override
	public String toString() {
		return "Cash [accumulated=" + getAccumulated() + ", client=" + getClient() + "]";
	}

}
