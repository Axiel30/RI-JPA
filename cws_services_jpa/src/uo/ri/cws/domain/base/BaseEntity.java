package uo.ri.cws.domain.base;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {

	public enum EntityState {
		ENABLED, 
		DISABLED // soft deleted
	};
	
	@Id 
	@Column(name = "ID")
	private String id = UUID.randomUUID().toString();
	
	@Column(name = "CREATEDAT")
	private LocalDateTime createdAt = LocalDateTime.now(); 
	
	@Column(name = "UPDATEDAT")
	private LocalDateTime updatedAt = createdAt;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ENTITYSTATE")
	private EntityState entityState = EntityState.ENABLED;
	
	@Version
	@Column(name = "VERSION")
	private long version = 1;
	
	public String getId() {
		return id;
	}

	public EntityState getEntityState() {
		return entityState;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public long getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@PreUpdate
	public void preUpdate() {
		updatedNow();
	}

	public void updatedNow() {
		updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}