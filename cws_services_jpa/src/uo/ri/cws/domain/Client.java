package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class Client {

    // Natural attributes
    private Nif nif;
    private FullName fullName;
    private Email email;
    private PhoneNumber phone;
    private Address address;

    // Accidental attributes
    private Set<Vehicle> vehicles = new HashSet<>();
    private Set<PaymentMean> paymentMeans = new HashSet<>();
    private Set<Recommendation> recommendations = new HashSet<>();
    private Recommendation recommendation;

    // ---- Constructors ----
    public Client(Nif nif) {
        this(nif,
                new FullName("No name", "No surname"),
                new Email("default@example.com"),
                new PhoneNumber("+34600000000"),
                new Address("No street", "No city", "No zip", "No country"));
    }

    public Client(Nif nif, FullName fullName) {
        this(nif, fullName, new Email("default@example.com"), new PhoneNumber("+34600000000"),
                new Address("No street", "No city", "No zip", "No country"));
    }

    public Client(Nif nif, FullName fullName, Email email, PhoneNumber phone, Address address) {
        ArgumentChecks.isNotNull(nif, "NIF must not be null");
        ArgumentChecks.isNotNull(fullName, "Full name must not be null");
        ArgumentChecks.isNotNull(email, "Email must not be null");
        ArgumentChecks.isNotNull(phone, "Phone must not be null");
        ArgumentChecks.isNotNull(address, "Address must not be null");
        this.nif = nif;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // ---- Getters ----
    public Nif getNif() { return nif; }
    public FullName getFullName() { return fullName; }
    public Email getEmail() { return email; }
    public PhoneNumber getPhone() { return phone; }
    public Address getAddress() { return address; }

    public Set<Vehicle> getVehicles() {
        return new HashSet<>(vehicles);
    }

    public Set<PaymentMean> getPaymentMeans() {
        return new HashSet<>(paymentMeans);
    }

    public Set<Recommendation> getRecommendations() {
        return new HashSet<>(recommendations);
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    // ---- Business methods ----
    public boolean hasPaidInvoices() {
        return vehicles.stream()
                .flatMap(v -> v.getWorkOrders().stream())
                .map(WorkOrder::getInvoice)
                .filter(Objects::nonNull)
                .anyMatch(Invoice::isSettled);
    }

    public List<Recommendation> getRecommendationsAvailableForVoucher25() {
        return recommendations.stream()
                .filter(r -> !r.isUsedForVoucher())
                .filter(r -> r.getRecommended() != null && r.getRecommended().hasPaidInvoices())
                .toList();
    }

    public boolean isEligibleForVoucher25() {
        return hasPaidInvoices() && getRecommendationsAvailableForVoucher25().size() >= 3;
    }

    public void markThreeRecommendationsAsUsedForVoucher25() {
        List<Recommendation> available = getRecommendationsAvailableForVoucher25();
        StateChecks.isTrue(available.size() >= 3, "Not enough recommendations available for voucher");
        for (int i = 0; i < 3; i++) {
            available.get(i).markAsUsedForVoucher();
        }
    }

    public List<WorkOrder> getWorkOrdersAvailableForVoucher20() {
        return vehicles.stream()
                .flatMap(v -> v.getWorkOrders().stream())
                .filter(wo -> wo.getInvoice() != null && wo.getInvoice().isSettled())
                .filter(wo -> !wo.isUsedForVoucher())
                .toList();
    }

    public void markThreeWorkOrdersAsUsedForVoucher20() {
        List<WorkOrder> available = getWorkOrdersAvailableForVoucher20();
        StateChecks.isTrue(available.size() >= 3, "Not enough work orders available for voucher");
        for (int i = 0; i < 3; i++) {
            available.get(i).markAsUsedForVoucher();
        }
    }

    // ---- Association helpers ----
    Set<Vehicle> _getVehicles() { return vehicles; }
    Set<PaymentMean> _getPaymentMeans() { return paymentMeans; }
    Set<Recommendation> _getRecommendations() { return recommendations; }
    void _setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Client other = (Client) obj;
        return Objects.equals(nif, other.nif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nif);
    }

    @Override
    public String toString() {
        return "Client[nif=" + nif + ", name=" + fullName + "]";
    }
}
