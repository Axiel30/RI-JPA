package uo.ri.cws.infrastructure.persistence.jpa.util;

import jakarta.persistence.PreUpdate;
import uo.ri.cws.domain.base.BaseEntity;

/**
 * EntityListener is a JPA entity listener that listens for entity lifecycle events.
 */
public class EntityListener {

	/**
	 * This method is called before an entity is updated in the database.
	 * It updates the 'updatedAt' field of the entity to the current timestamp.
	 * Must be attached to BaseEntity subclasses using the @EntityListeners annotation.
	 *
	 * @param entity The entity being updated.
	 */
	@PreUpdate
    public void preUpdate(BaseEntity entity) {
    	entity.updatedNow();
    }
	
}
