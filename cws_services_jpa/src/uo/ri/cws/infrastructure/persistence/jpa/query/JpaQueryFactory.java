package uo.ri.cws.infrastructure.persistence.jpa.query;

import uo.ri.cws.application.query.ClientQueries;
import uo.ri.cws.application.query.InvoiceQueries;
import uo.ri.cws.application.query.MechanicQueries;
import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.query.QueryFactory;

public class JpaQueryFactory implements QueryFactory {

	@Override
	public ClientQueries forClient() {
		return new ClientJpaQueries();
	}

	@Override
	public MechanicQueries forMechanic() {
		return new MechanicJpaQueries();
	}

	@Override
	public PaymentMeanQueries forPaymentMean() {
		return new PaymentMeanJpaQueries();
	}

	@Override
	public InvoiceQueries forInvoice() {
		return new InvoiceJpaQueries();
	}

}
