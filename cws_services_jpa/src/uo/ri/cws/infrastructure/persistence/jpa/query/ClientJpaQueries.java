package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.ClientQueries;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.util.assertion.ArgumentChecks;

public class ClientJpaQueries implements ClientQueries {

	@Override
	public boolean hasPaidInvoices(String clientId) {
		ArgumentChecks.isNotEmpty(clientId);

		return 0 < Jpa.getManager()
				.createNamedQuery("Client.hasPaidInvoices", Long.class)
				.setParameter(1, clientId)
				.getSingleResult();
	}

}
