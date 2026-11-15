package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

@Entity
@Table(name = "TCLIENTS")
public class Client extends BaseEntity {

    // Natural attributes
    @Embedded
    @AttributeOverride(name = "nif", column = @Column(name = "NIF", unique = true))
    private Nif nif;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "NAME"))
    @AttributeOverride(name = "surname", column = @Column(name = "SURNAME"))
    private FullName fullName;

    @Embedded
    @AttributeOverride(name = "email", column = @Column(name = "EMAIL"))
    private Email email;

    @Embedded
    @AttributeOverride(name = "phone", column = @Column(name = "PHONE"))
    private PhoneNumber phone;

    @Embedded
    @AttributeOverride(name = "street", column = @Column(name = "STREET"))
    @AttributeOverride(name = "city", column = @Column(name = "CITY"))
    @AttributeOverride(name = "country", column = @Column(name = "COUNTRY"))
    @AttributeOverride(name = "postalCode", column = @Column(name = "POSTALCODE"))
    private Address address;

    // Accidental attributes
    @OneToMany(mappedBy = "client")
    private Set<Vehicle> vehicles = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private Set<PaymentMean> paymentMeans = new HashSet<>();

    @OneToMany(mappedBy = "recommender")
    private Set<Recommendation> recommendations = new HashSet<>();

    @OneToOne(mappedBy = "recommended")
    private Recommendation recommendation;

    // ---- Constructors ----
    protected Client() {
    }

    public Client(Nif nif) {
        this(nif, new FullName("No name", "No surname"), new Email("default@example.com"),
            new PhoneNumber("+34600000000"), new Address("No street", "No city", "No zip", "No country"));
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
    public Nif getNif() {
        return nif;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Email getEmail() {
        return email;
    }

    public PhoneNumber getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

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
        return vehicles.stream().flatMap(v -> v.getWorkOrders().stream()).map(WorkOrder::getInvoice)
            .filter(Objects::nonNull).anyMatch(Invoice::isSettled);
    }

    public List<Recommendation> getRecommendationsAvailableForVoucher25() {
        return recommendations.stream().filter(r -> !r.isUsedForVoucher())
            .filter(r -> r.getRecommended() != null && r.getRecommended().hasPaidInvoices()).toList();
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
        return vehicles.stream().flatMap(v -> v.getWorkOrders().stream())
            .filter(wo -> wo.getInvoice() != null && wo.getInvoice().isSettled()).filter(wo -> !wo.isUsedForVoucher())
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
    Set<Vehicle> _getVehicles() {
        return vehicles;
    }

    Set<PaymentMean> _getPaymentMeans() {
        return paymentMeans;
    }

    Set<Recommendation> _getRecommendations() {
        return recommendations;
    }

    void _setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    @Override
    public String toString() {
        return "Client[nif=" + nif + ", name=" + fullName + "]";
    }
}
