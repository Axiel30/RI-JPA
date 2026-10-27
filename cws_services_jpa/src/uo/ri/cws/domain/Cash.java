package uo.ri.cws.domain;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class Cash extends PaymentMean {

	// ---- Constructors ----
	
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
