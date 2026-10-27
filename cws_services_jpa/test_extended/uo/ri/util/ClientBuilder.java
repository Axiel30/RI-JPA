package uo.ri.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;

public class ClientBuilder implements Builder<Client> {
	private static final String NIF_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
	private static final AtomicInteger NIF_SEQUENCE = new AtomicInteger(10_000_000);
	private static final AtomicLong INVOICE_SEQUENCE = new AtomicLong(1);

	private Nif nif = nextNif();
	private FullName fullName = new FullName(Values.newName(), Values.newSurname());
	private int paidWorkOrders;
	private int unpaidWorkOrders;
	private int uninvoicedWorkOrders;

	@Override
	public Client build() {
		Client client = new Client(nif, fullName);
		for (int i = 0; i < paidWorkOrders; i++) {
			addInvoiceTo(client, true);
		}
		for (int i = 0; i < unpaidWorkOrders; i++) {
			addInvoiceTo(client, false);
		}
		for (int i = 0; i < uninvoicedWorkOrders; i++) {
			addApprovedWorkOrderTo(client);
		}
		return client;
	}

	public ClientBuilder withNif(String nif) {
		this.nif = new Nif(nif);
		return this;
	}

	public ClientBuilder withFullName(String name, String surname) {
		this.fullName = new FullName(name, surname);
		return this;
	}

	public ClientBuilder withPaidInvoice() {
		this.paidWorkOrders++;
		return this;
	}

	public ClientBuilder withUnpaidInvoice() {
		this.unpaidWorkOrders++;
		return this;
	}

	public ClientBuilder withPaidWorkOrder() {
		return withPaidInvoice();
	}

	public ClientBuilder withUnpaidWorkOrder() {
		return withUnpaidInvoice();
	}

	public ClientBuilder withUninvoicedWorkOrder() {
		this.uninvoicedWorkOrders++;
		return this;
	}

	public ClientBuilder withPaidWorkOrders(int number) {
		this.paidWorkOrders += number;
		return this;
	}

	public ClientBuilder withUnpaidWorkOrders(int number) {
		this.unpaidWorkOrders += number;
		return this;
	}

	public ClientBuilder withUninvoicedWorkOrders(int number) {
		this.uninvoicedWorkOrders += number;
		return this;
	}

	private void addInvoiceTo(Client client, boolean paid) {
		WorkOrder workOrder = addApprovedWorkOrderTo(client);
		Invoice invoice = new Invoice(
			INVOICE_SEQUENCE.getAndIncrement(),
			List.of(workOrder)
		);
		if (paid) {
			PaymentMean cash = new Cash(client);
			invoice.settle(Map.of(cash, invoice.getTotal()));
		}
	}

	private WorkOrder addApprovedWorkOrderTo(Client client) {
		return new WorkOrderBuilder()
			.forClient(client)
			.build();
	}

	private static Nif nextNif() {
		int number = NIF_SEQUENCE.getAndIncrement();
		char control = NIF_LETTERS.charAt(number % NIF_LETTERS.length());
		return new Nif(String.format("%08d%c", number, control));
	}

}
