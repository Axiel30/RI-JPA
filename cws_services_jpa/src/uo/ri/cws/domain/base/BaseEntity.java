package uo.ri.cws.domain.base;

import java.time.LocalDateTime;
import java.util.UUID;
public abstract class BaseEntity {

	public enum EntityState {
		ENABLED, 
		DISABLED // soft deleted
	};
	
	private String id = UUID.randomUUID().toString();
	
	private LocalDateTime createdAt = LocalDateTime.now(); 
	
	private LocalDateTime updatedAt = createdAt;
	
	private EntityState entityState = EntityState.ENABLED;
	
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