package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Mechanic;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a mechanic are persisted and can be reloaded correctly
 * - A mechanic cannot be persisted if another one with the same NIF already exists
 */
class MechanicMappingTests {

	private Mechanic mechanic;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		mechanic = new Mechanic(new Nif("12345678Z"), new FullName("Name", "Surname"));
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( mechanic );
		factory.close();
	}

	/**
	 * GIVEN: a mechanic persisted with all its fields set
	 * WHEN: the mechanic is reloaded from the database
	 * THEN: all its fields (id, nif and full name) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(mechanic);

		Mechanic restored = unitOfWork.findById(Mechanic.class, mechanic.getId() );

		assertEquals( mechanic.getId(), restored.getId() );
		assertEquals( mechanic.getNif(), restored.getNif() );
		assertEquals( mechanic.getFullName(), restored.getFullName() );
	}

	/**
	 * GIVEN: a persisted mechanic
	 * WHEN: a new mechanic with the same NIF is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(mechanic);
		Mechanic repeatedNifMechanic = new Mechanic( mechanic.getNif() );

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeatedNifMechanic ) 
			);
	}

}