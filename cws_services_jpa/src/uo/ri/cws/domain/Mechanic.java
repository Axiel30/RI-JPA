package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;

public class Mechanic extends BaseEntity {

	// Natural attributes
	private Nif nif;

	private FullName fullName;

	// Accidental attributes
	private Set<WorkOrder> assigned = new HashSet<>();
	
	// ---- Constructors ----
	protected Mechanic() {}

	public Mechanic(Nif nif) {
		this(nif, new FullName("No name", "No surname"));
	}

	public Mechanic(Nif nif, FullName name) {
		ArgumentChecks.isNotNull(nif, "NIF must not be null");
		ArgumentChecks.isNotNull(name, "Full name must not be null");
		this.nif = nif;
		this.fullName = name;
	}

	// ---- Getters ----
	public Nif getNif() { return nif; }
	public FullName getFullName() { return fullName; }

	public Set<WorkOrder> getAssigned() {
		return new HashSet<>(assigned);
	}

	@Override
	public String toString() {
		return "Mechanic[nif=" + nif + ", name=" + fullName + "]";
	}

	// ---- Business methods ----

	public void updatePersonalData(Nif nif, FullName name) {
		ArgumentChecks.isNotNull(nif, "NIF must not be null");
		ArgumentChecks.isNotNull(name, "Full name must not be null");
		this.nif = nif;
		this.fullName = name;
	}

	// ---- Associations helpers ----

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

}
