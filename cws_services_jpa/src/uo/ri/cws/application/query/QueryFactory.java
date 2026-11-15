package uo.ri.cws.application.query;

public interface QueryFactory {

	ClientQueries forClient();
	MechanicQueries forMechanic();
	PaymentMeanQueries forPaymentMean();
	InvoiceQueries forInvoice();

}
