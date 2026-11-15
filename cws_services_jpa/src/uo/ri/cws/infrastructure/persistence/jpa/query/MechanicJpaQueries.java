package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.MechanicQueries;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.util.assertion.ArgumentChecks;

public class MechanicJpaQueries implements MechanicQueries {

	@Override
	public boolean hasInterventions(String mechanicId) {
		ArgumentChecks.isNotEmpty(mechanicId);

		return 0 < Jpa.getManager()
				.createNamedQuery("Mechanic.countInterventions", Long.class)
				.setParameter(1, mechanicId)
				.getSingleResult();
	}

}
