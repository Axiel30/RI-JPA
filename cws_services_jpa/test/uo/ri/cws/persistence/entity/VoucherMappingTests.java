package uo.ri.cws.persistence.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - All fields of a voucher are persisted and can be reloaded correctly
 * - A voucher cannot be persisted if another one with the same code already exists
 */
class VoucherMappingTests {

	private Client client;
	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Voucher voucher;
	private static final Money ONE_HUNDRED = Money.of("100.0");
	private static final Money FIFTY = Money.of("50.0");

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

		voucher = new Voucher(
				new VoucherCode("voucher-code")
				, "voucher-description"
				, ONE_HUNDRED);

		Associations.Holds.link(client, voucher);

		unitOfWork.persist(client, voucher);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( client, voucher );
		factory.close();
	}

	/**
	 * GIVEN: a voucher held by a client, both persisted
	 * WHEN: the voucher is reloaded from the database
	 * THEN: all its fields (id, code, description, accumulated and available amount) match the original values
	 */
	@Test
	void testAllFieldsPersisted() {
		Voucher restored = unitOfWork.findById( Voucher.class, voucher.getId());

		assertEquals( voucher.getId(), restored.getId() );
		assertEquals( voucher.getCode(), restored.getCode() );
		assertEquals( voucher.getDescription(), restored.getDescription() );
		assertEquals( voucher.getAccumulated(), restored.getAccumulated() );
		assertEquals( voucher.getAvailableAmount(), restored.getAvailableAmount() );
	}

	/**
	 * GIVEN: a persisted voucher
	 * WHEN: a new voucher with the same code is persisted
	 * THEN: a PersistenceException is thrown
	 */
	@Test
	void testRepeated() {
		Voucher repeated = new Voucher( voucher.getCode(),"another-voucher", FIFTY);

		assertThrows(PersistenceException.class, 
				() -> unitOfWork.persist( repeated )
			);
	}
}
