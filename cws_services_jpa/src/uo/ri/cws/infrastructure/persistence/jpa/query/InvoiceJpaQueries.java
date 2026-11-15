package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.InvoiceQueries;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;

public class InvoiceJpaQueries implements InvoiceQueries {

	@Override
	public Long getNextInvoiceNumber() {
		return Jpa.getManager()
				.createNamedQuery("Invoice.getNextInvoiceNumber", Long.class)
				.getSingleResult();
	}

}
