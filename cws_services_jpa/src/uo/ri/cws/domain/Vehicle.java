package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TVEHICLES")
public class Vehicle extends BaseEntity {

	// Natural attributes
	@Embedded
	@AttributeOverride(name = "licensePlate", column = @Column(name = "LICENSEPLATE", unique = true))
	private LicensePlate licensePlate;

	@Column(name = "MAKE")
	private String make;

	@Column(name = "MODEL")
	private String model;
	
	// Accidental attributes
	@ManyToOne
	@JoinColumn(name = "VEHICLETYPE_ID")
	private VehicleType vehicleType;

	@ManyToOne
	@JoinColumn(name = "CLIENT_ID")
	private Client client;

	@OneToMany(mappedBy = "vehicle")
	private Set<WorkOrder> workOrders = new HashSet<>();

	// ---- Constructors ----
	protected Vehicle() {}

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
	public String toString() {
		return "Vehicle[plate=" + licensePlate + ", make=" + make + ", model=" + model + "]";
	}

}
