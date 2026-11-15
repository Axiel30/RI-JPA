package uo.ri.cws.persistence.association;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Vehicle;
import uo.ri.cws.domain.types.Address;
import uo.ri.cws.domain.types.Email;
import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;
import uo.ri.cws.domain.types.PhoneNumber;
import uo.ri.cws.persistence.util.UnitOfWork;

/**
 * Scenarios:
 * - A client recovers its owned vehicles after being persisted and reloaded
 * - A vehicle recovers its owner client after being persisted and reloaded
 */
class OwnsMappingTests {

	private UnitOfWork unitOfWork;
	private EntityManagerFactory factory;
	private Vehicle vehicle;
	private Client client;

	@BeforeEach
	void setUp() {
		factory = Persistence.createEntityManagerFactory("carworkshop");
		unitOfWork = UnitOfWork.over( factory );

		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");

        client = new Client(
                new Nif("12345678Z"),
                new FullName("Name","Surname"),
                new Email("test@example.com"),
                new PhoneNumber("+34600123123"),
                new Address("street", "city", "33000", "ES")
        );

		Associations.Owns.link(client, vehicle);

		unitOfWork.persist(vehicle, client);
	}

	@AfterEach
	void tearDown() {
		unitOfWork.remove( vehicle, client );
		factory.close();
	}

	/**
	 * GIVEN: a client owning a vehicle, both persisted
	 * WHEN: the client is reloaded from the database
	 * THEN: the reloaded client's vehicles contains that vehicle
	 */
	@Test
	void testClientRecoversVehicles() {

		Client restored = unitOfWork.findById( Client.class, client.getId() );

		assertFalse( restored.getVehicles().isEmpty() );
		assertEquals( 1, restored.getVehicles().size() );
	}

	/**
	 * GIVEN: a client owning a vehicle, both persisted
	 * WHEN: the vehicle is reloaded from the database
	 * THEN: the reloaded vehicle's client equals the given client
	 */
	@Test
	void testVehicleRecoversClient() {

		Vehicle restored = unitOfWork.findById( Vehicle.class, vehicle.getId() );

		assertEquals( client, restored.getClient() );
	}

}