package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a client are persisted and can be reloaded correctly
 * - A client cannot be persisted if another client with the same NIF already exists
 */
class ClientMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

        client = new Client(
                new Nif("12345678Z"),
                new FullName("Name", "Surname"),
                new Email("test@email.com"),
                new PhoneNumber("+34600123123"),
                new Address("street", "city", "33000", "ES")
        );
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client );
		factory.close();
	}

	/**
	 * GIVEN: a client persisted with all its fields set
	 * WHEN: the client is reloaded from the database
	 * THEN: all its fields (id, nif, full name, email, phone and address) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		unitOfWork.persist(client);

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertEquals( client.getId(), restored.getId() );
		assertEquals( client.getNif(), restored.getNif() );
		assertEquals( client.getFullName(), restored.getFullName() );
		assertEquals( client.getEmail(), restored.getEmail() );
		assertEquals( client.getPhone(), restored.getPhone() );
		assertEquals( client.getAddress(), restored.getAddress() );
	}

	/**
	 * GIVEN: a persisted client
	 * WHEN: a new client with the same NIF is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		unitOfWork.persist(client);
		Client repeatedNifClient = new Client( client.getNif() );

		assertThrows( PersistenceException.class, () -> {
			unitOfWork.persist( repeatedNifClient );
		});
	}
}