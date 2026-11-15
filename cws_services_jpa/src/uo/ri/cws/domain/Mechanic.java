package uo.ri.cws.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import uo.ri.cws.domain.base.BaseEntity;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;

@Entity
@Table(name = "TMECHANICS")
public class Mechanic extends BaseEntity {

	// Natural attributes
	@Embedded
	@AttributeOverride(name = "nif", column = @Column(name = "NIF", unique = true))
	private Nif nif;

	@Embedded
	@AttributeOverride(name = "name", column = @Column(name = "NAME"))
	@AttributeOverride(name = "surname", column = @Column(name = "SURNAME"))
	private FullName fullName;

	// Accidental attributes
	@OneToMany(mappedBy = "mechanic")
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

	// ---- Setters (optional, controlled) ----

	public void setFullName(FullName name) {
		ArgumentChecks.isNotNull(name);
		this.fullName = name;
	}
	
	public void setNif(Nif nif) {
		ArgumentChecks.isNotNull(nif);
		this.nif = nif;		
	}

	// ---- Associations helpers ----

	Set<WorkOrder> _getAssigned() {
		return assigned;
	}

}
