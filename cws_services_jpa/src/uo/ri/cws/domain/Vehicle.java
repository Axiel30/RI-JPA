package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class Vehicle {

	// Natural attributes
	private LicensePlate licensePlate;
	private String make;
	private String model;
	
	// Accidental attributes
	private VehicleType vehicleType;
	private Client client;
	private Set<WorkOrder> workOrders = new HashSet<>();

	// ---- Constructors ----

	public Vehicle(LicensePlate plateNumber) {
		this(plateNumber, "UNKNOWN", "UNKNOWN");
	}

	public Vehicle(LicensePlate plateNumber, String make, String model) {
		ArgumentChecks.isNotNull(plateNumber, "License plate must not be null");
		ArgumentChecks.isNotBlank(make, "Make must not be blank");
		ArgumentChecks.isNotBlank(model, "Model must not be blank");
		this.licensePlate = plateNumber;
		this.make = make;
		this.model = model;
	}

	public Money getPricePerHour() {
		return vehicleType.getPricePerHour(); // VehicleType must return Money
	}

	// ---- Getters ----
	public LicensePlate getLicensePlate() {
		return licensePlate;
	}

	public String getMake() {
		return make;
	}

	public String getModel() {
		return model;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public Client getClient() {
		return client;
	}

	public Set<WorkOrder> getWorkOrders() {
		return new HashSet<>(workOrders);
	}

	// ---- Association helpers ----
	/* package */ void _setVehicleType(VehicleType vt) {
		this.vehicleType = vt;
	}

	/* package */ void _setClient(Client client) {
		this.client = client;
	}

	/* package */ Set<WorkOrder> _getWorkOrders() {
		return workOrders;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vehicle other = (Vehicle) obj;
		return Objects.equals(licensePlate, other.licensePlate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(licensePlate);
	}

	@Override
	public String toString() {
		return "Vehicle[plate=" + licensePlate + ", make=" + make + ", model=" + model + "]";
	}

}
