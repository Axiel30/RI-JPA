package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;

public class VehicleType {
    // Natural attributes
    private String name;
    private Money pricePerHour;

    // Accidental attributes
    private Set<Vehicle> vehicles = new HashSet<>();

    // ---- Constructors ----
    public VehicleType(String name, Money pricePerHour) {
        ArgumentChecks.isNotNull(name, "Name must not be null");
        ArgumentChecks.isNotBlank(name, "Name must not be blank");
        ArgumentChecks.isNotNull(pricePerHour, "Price per hour must not be null");
        ArgumentChecks.isTrue(pricePerHour.greaterOrEqual(Money.ZERO), "Price per hour must be non‑negative");
        this.name = name;
        this.pricePerHour = Money.calc(pricePerHour).toMoneyRoundedToCents();
    }

    // ---- Getters ----
    public String getName() { return name; }
    public Money getPricePerHour() { return pricePerHour; }

    // ---- Association helpers ----
    Set<Vehicle> _getVehicles() { return vehicles; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VehicleType other = (VehicleType) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "VehicleType[name=" + name + "]";
    }
}
