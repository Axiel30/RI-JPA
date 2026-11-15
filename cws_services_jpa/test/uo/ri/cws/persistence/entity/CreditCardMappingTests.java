package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.YearMonth;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.CreditCardNumber;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a credit card are persisted and can be reloaded correctly
 * - A credit card cannot be persisted if another card with the same number already exists
 */
class CreditCardMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private CreditCard creditCard;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		client = new Client(
		        new Nif("12345678Z"),
		        new FullName("Name", "Surname"),
		        new Email("test@example.com"),
		        new PhoneNumber("+34600123123"),
		        new Address("street", "city", "33000", "ES")
		);
		
		creditCard = new CreditCard(
				new CreditCardNumber("4111111111111111")
				, "visa"
				, YearMonth.now().plusYears(1)
			);
		Associations.Holds.link(client, creditCard);

		unitOfWork.persist(client, creditCard);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client, creditCard );
		factory.close();
	}

	/**
	 * GIVEN: a credit card held by a client, both persisted
	 * WHEN: the credit card is reloaded from the database
	 * THEN: all its fields (id, accumulated, number, type and valid thru) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		CreditCard restored = unitOfWork.findById( CreditCard.class,
				creditCard.getId()
			);

		assertEquals( creditCard.getId(), restored.getId() );
		assertEquals( creditCard.getAccumulated(), restored.getAccumulated() );
		assertEquals( creditCard.getNumber(), restored.getNumber() );
		assertEquals( creditCard.getType(), restored.getType() );
		assertEquals( creditCard.getValidThru(), restored.getValidThru() );
	}

	/**
	 * GIVEN: a persisted credit card
	 * WHEN: a new card with the same number is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		CreditCard repeated = new CreditCard( 
				creditCard.getNumber(), // repeated number
				"another-card-type",
				YearMonth.now()
			);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeated ) 
			);
	}

}
