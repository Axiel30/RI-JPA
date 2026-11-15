package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.SparePart;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a spare part are persisted and can be reloaded correctly
 * - A spare part cannot be persisted if another one with the same code already exists
 */
class SparePartMappingTests {

	private SparePart sparePart;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private static final Money ONE_HUNDRED_WITH_SOMETHING = Money.of("100.1234567890");

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		sparePart = new SparePart("code", "description", ONE_HUNDRED_WITH_SOMETHING);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( sparePart );
		factory.close();
	}

	/**
	 * GIVEN: a spare part persisted with all its fields set
	 * WHEN: the spare part is reloaded from the database
	 * THEN: all its fields (id, code, description and price) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(sparePart);

		SparePart restored = unitOfWork.findById( SparePart.class, sparePart.getId() );

		assertEquals( sparePart.getId(), restored.getId() );
		assertEquals( sparePart.getCode(), restored.getCode() );
		assertEquals( sparePart.getDescription(), restored.getDescription() );
		assertEquals( sparePart.getPrice(), restored.getPrice() );
	}

	/**
	 * GIVEN: a persisted spare part
	 * WHEN: a new spare part with the same code is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(sparePart);
		SparePart repeatedCodeSparePart = new SparePart( sparePart.getCode() );

		assertThrows( PersistenceException.class, () -> {
			unitOfWork.persist( repeatedCodeSparePart );
		});
	}

}
