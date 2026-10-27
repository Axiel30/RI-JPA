package uo.ri.util;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.VehicleType;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;

public class WorkOrderBuilder implements Builder<WorkOrder> {
	private static final String NIF_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
	private static final AtomicInteger NIF_SEQUENCE = new AtomicInteger(40_000_000);
	private static final AtomicInteger PLATE_SEQUENCE = new AtomicInteger(1_000);
	private static final AtomicInteger VEHICLE_TYPE_SEQUENCE = new AtomicInteger(1);

	private Client client;
	private Money amount = Money.of("60.00");

	@Override
	public WorkOrder build() {
		Client owner = client == null ? new ClientBuilder().build() : client;
		Vehicle vehicle = new Vehicle(nextPlate(), "make", "model");
		VehicleType vehicleType = new VehicleType(
			"type-" + VEHICLE_TYPE_SEQUENCE.getAndIncrement(),
			amount
		);
		Associations.Owns.link(owner, vehicle);
		Associations.Classifies.link(vehicleType, vehicle);

		WorkOrder workOrder = new WorkOrder(
			vehicle,
			LocalDateTime.now(),
			"test work order"
		);
		Mechanic mechanic = new Mechanic(nextNif());
		workOrder.assignTo(mechanic);
		workOrder.addIntervention(LocalDateTime.now(), 60);
		workOrder.finish();
		workOrder.approve();
		return workOrder;
	}

	public WorkOrderBuilder forClient(Client client) {
		this.client = client;
		return this;
	}

	public WorkOrderBuilder withAmount(String amount) {
		this.amount = Money.of(amount);
		return this;
	}

	private static Nif nextNif() {
		int number = NIF_SEQUENCE.getAndIncrement();
		char control = NIF_LETTERS.charAt(number % NIF_LETTERS.length());
		return new Nif(String.format("%08d%c", number, control));
	}

	private static LicensePlate nextPlate() {
		return new LicensePlate(PLATE_SEQUENCE.getAndIncrement(), "GJK");
	}
}
