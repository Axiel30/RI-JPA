package uo.ri.cws.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import uo.ri.cws.domain.types.Money;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.assertion.StateChecks;

public class WorkOrder {

    public enum WorkOrderState {
        OPEN,
        ASSIGNED,
        FINISHED,
        APPROVED,   
        INVOICED
    }

    // Natural attributes
    private LocalDateTime date;
    private String description;
    private Money amount = Money.ZERO;  // cents
    private WorkOrderState state = WorkOrderState.OPEN;
    private boolean usedForVoucher = false;

    // Accidental attributes
    private Invoice invoice;
    private Mechanic mechanic;
    private Vehicle vehicle;
    private Set<Intervention> interventions = new HashSet<>();

    // ---- Constructors ----

    public WorkOrder(Vehicle vehicle) {
        this(vehicle, LocalDateTime.now(), "no-description");
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date) {
        this(vehicle, date, "no-description");
    }

    public WorkOrder(Vehicle vehicle, String description) {
        this(vehicle, LocalDateTime.now(), description);
    }

    public WorkOrder(Vehicle vehicle, LocalDateTime date, String description) {
    	ArgumentChecks.isNotNull(vehicle, "Vehicle must not be null");
    	ArgumentChecks.isNotNull(date, "Date must not be null");
    	ArgumentChecks.isNotNull(description, "Description must not be null");
    	ArgumentChecks.isNotBlank(description, "Description must not be blank");
    	this.date = date.truncatedTo(ChronoUnit.MILLIS);
    	this.description = description;
    	Associations.Fixes.link(vehicle, this);
    }

    // ---- Getters ----
    public String getDescription() {
        return description;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Money getPricePerHour() {
        return getVehicle().getPricePerHour();
    }

    public Set<Intervention> getInterventions() {
        return new HashSet<>(interventions);
    }

    public WorkOrderState getState() {
        return state;
    }

    public boolean isUsedForVoucher() {
        return usedForVoucher;
    }

    public void markAsUsedForVoucher() {
        this.usedForVoucher = true;
    }

    // ---- Association helpers ----
    void _setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    void _setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    void _setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    Set<Intervention> _getInterventions() {
        return interventions;
    }

    // ---- State checks ----
    public boolean isOpen() {
        return state == WorkOrderState.OPEN;
    }

    public boolean isAssigned() {
        return state == WorkOrderState.ASSIGNED;
    }

    public boolean isFinished() {
        return state == WorkOrderState.FINISHED;
    }

    public boolean isApproved() {
        return state == WorkOrderState.APPROVED;
    }

    public boolean isInvoiced() {
        return state == WorkOrderState.INVOICED;
    }

    // ---- Business methods & state transitions ----
    
    /**
	 * Links (assigns) the work order to a mechanic and then changes its state to ASSIGNED
	 * @throws IllegalStateException if The work order is not in OPEN state
	 */
    public void assignTo(Mechanic mechanic) {
    	StateChecks.isTrue(isOpen(), "Work order must be in OPEN state");
    	Associations.Assigns.link(mechanic, this);
    	this.state = WorkOrderState.ASSIGNED;
    }

    /**
	 * Unlinks (unassigns) the work order from its mechanic and then changes its state to OPEN
	 * @throws IllegalStateException if The work order is not in ASSIGNED state
	 */
    public void unassign() {
    	StateChecks.isTrue(isAssigned(), "Work order must be in ASSIGNED state");
    	Associations.Assigns.unlink(this);
    	this.state = WorkOrderState.OPEN;
    }

    /**
	 * Given the right conditions unlinks the workorder and the mechanic, 
	 * changes the state to FINISHED
	 * @throws IllegalStateException if the work order is not in ASSIGNED state
     */
    public void finish() {
    	StateChecks.isTrue(isAssigned(), "Work order must be in ASSIGNED state");
    	Associations.Assigns.unlink(this);
    	this.state = WorkOrderState.FINISHED;
    }

    /**
	 * Given the right conditions changes the state to APPROVED and computes the amount
	 * @throws IllegalStateException if the work order is not in FINISHED state
	 */
    public void approve() {
    	StateChecks.isTrue(isFinished(), "Work order must be in FINISHED state");
    	this.state = WorkOrderState.APPROVED;
    	computeAmount();
    }

    /**
	 * Given the right conditions changes the state to INVOICED
	 * @throws IllegalStateException if the work order is not in APPROVED state or if it has no invoice assigned
	 */
    public void invoice() {
    	StateChecks.isTrue(isApproved(), "Work order must be in APPROVED state");
    	StateChecks.isTrue(invoice != null, "Work order has no invoice assigned");
    	this.state = WorkOrderState.INVOICED;
    }

    /**
	 * Changes back the state to FINISHED, given the right conditions
	 * @throws IllegalStateException if the work order is not in APPROVED state
	 */
    public void unapprove() {
    	StateChecks.isTrue(isApproved(), "Work order must be in APPROVED state");
    	this.state = WorkOrderState.FINISHED;
    	this.amount = Money.ZERO;
    }

    /**
	 * Changes back the state to APPROVED, given the right conditions
	 * @throws IllegalStateException if the work order is not in INVOICED state
	 */
    public void uninvoice() {
    	StateChecks.isTrue(isInvoiced(), "Work order must be in INVOICED state");
    	this.state = WorkOrderState.APPROVED;
    }

    /**
     * Changes a finished work order to OPEN
     * @throws IllegalStateException if the work order is not in FINISHED state
     */
    public void reopen() {
    	StateChecks.isTrue(isFinished(), "Work order must be in FINISHED state");
    	this.state = WorkOrderState.OPEN;
    	this.amount = Money.ZERO;
    }

    
    // ---- Intervention management ----
	public void addIntervention(LocalDateTime date, int minutes) {
		addIntervention(date, minutes, Map.of());
	}

	public void addIntervention(LocalDateTime date, int minutes, Map<SparePart, Integer> substitutions) {
		StateChecks.isTrue(isAssigned(), "Work order not assigned");
		addIntervention(mechanic, date, minutes, substitutions);
	}

	public void addIntervention(Mechanic mechanic, LocalDateTime date, int minutes) {
		addIntervention(mechanic, date, minutes, Map.of());
	}

	public void addIntervention(
			Mechanic mechanic,
			LocalDateTime date,
			int minutes,
			Map<SparePart, Integer> substitutions) {
		ArgumentChecks.isNotNull(mechanic, "Mechanic cannot be null");
		ArgumentChecks.isNotNull(date, "Date cannot be null");
		ArgumentChecks.isTrue(minutes >= 0, "Minutes must be non-negative");
		ArgumentChecks.isNotNull(substitutions, "Substitutions map cannot be null");

		StateChecks.isTrue(isAssigned() || isFinished(), "Work order neither assigned nor finished");

		LocalDateTime truncated = date.truncatedTo(ChronoUnit.MILLIS);
		boolean duplicate = interventions.stream()
				.anyMatch(i -> Objects.equals(i.getMechanic(), mechanic)
						&& Objects.equals(i.getDate(), truncated));
		StateChecks.isFalse(duplicate, "Duplicate intervention");

		new Intervention(mechanic, this, truncated, minutes, substitutions);
	}

	public void removeIntervention(Intervention intervention) {
		ArgumentChecks.isNotNull(intervention);
		StateChecks.isTrue(isAssigned() || isFinished(), "Work order neither assigned nor finished");
		StateChecks.isTrue(interventions.contains(intervention), "Intervention does not belong to this work order");

		intervention.removeSubstitutions();
		Associations.RecordsIntervenes.atomicUnlink(intervention);
	}

    // ---- Amount calculation ----
    private void computeAmount() {
    	Money.MoneyCalculator calc = Money.calc();
    	for (Intervention i : interventions) {
    		calc.add(i.getTotalAmount());
    	}
    	this.amount = calc.toMoneyRoundedToCents();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorkOrder other = (WorkOrder) obj;
        return Objects.equals(date, other.date) && Objects.equals(vehicle, other.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, vehicle);
    }

    @Override
    public String toString() {
        return "WorkOrder [date=" + date + ", vehicle=" + vehicle + ", state=" + state + ", amount=" + amount + "]";
    }
}
