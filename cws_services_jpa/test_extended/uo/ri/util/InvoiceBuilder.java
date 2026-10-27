package uo.ri.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.WorkOrder;

public class InvoiceBuilder implements Builder<Invoice> {
	private static final AtomicLong INVOICE_SEQUENCE = new AtomicLong(10_000);

	private Client client = new ClientBuilder().build();
	private String subtotal = "100.00";
	private boolean paid;

	@Override
	public Invoice build() {
		WorkOrder workOrder = new WorkOrderBuilder()
			.forClient(client)
			.withAmount(subtotal)
			.build();
		Invoice invoice = new Invoice(
			INVOICE_SEQUENCE.getAndIncrement(),
			List.of(workOrder)
		);
		if (paid) {
			PaymentMean cash = new Cash(client);
			invoice.settle(Map.of(cash, invoice.getTotal()));
		}
		return invoice;
	}

	public InvoiceBuilder forClient(Client client) {
		this.client = client;
		return this;
	}

	public InvoiceBuilder withSubtotal(String subtotal) {
		this.subtotal = subtotal;
		return this;
	}

	public InvoiceBuilder paid() {
		this.paid = true;
		return this;
	}
}
