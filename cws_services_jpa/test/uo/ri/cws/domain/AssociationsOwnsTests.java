package uo.ri.cws.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uo.ri.cws.domain.types.FullName;
import uo.ri.cws.domain.types.LicensePlate;
import uo.ri.cws.domain.types.Nif;

/**
 * Scenarios:
 * - Linking a client and a vehicle updates both sides of the association
 * - Unlinking a client and a vehicle removes the link on both sides
 * - The collection returned by getVehicles() is a safe copy
 * - Linking rejects a null client
 * - Linking rejects a null vehicle
 * - Linking rejects a vehicle that already has a client
 * - Linking rejects a vehicle already present in the client's vehicles
 * - Unlinking rejects a null vehicle
 * - Unlinking rejects a vehicle without a client
 * - Unlinking rejects an inconsistent association
 */
class AssociationsOwnsTests {
	private Vehicle vehicle;
	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client(new Nif("12345678Z"), new FullName("Name", "Surname"));
		vehicle = new Vehicle(new LicensePlate("1234 GJK"), "seat", "ibiza");
	}
	
	/**
	 * GIVEN: a client and a vehicle linked through Associations.Owns
	 * WHEN: querying both objects
	 * THEN: the vehicle is in the client's vehicles
	 * AND: the client is the vehicle's client
	 */
	@Test
	void testLinkOnOwn() {
		Associations.Owns.link(client, vehicle);

		assertTrue( client.getVehicles().contains( vehicle ));
		assertSame( client, vehicle.getClient() );
	}

	/**
	 * GIVEN: a client and a vehicle linked
	 * WHEN: Associations.Owns.unlink is called
	 * THEN: the vehicle is removed from the client's vehicles
	 * AND: the vehicle's client is null
	 */
	@Test
	void testUnlinkOnOwn() {
		Associations.Owns.link(client, vehicle);

		Associations.Owns.unlink(vehicle);

		assertFalse( client.getVehicles().contains( vehicle ));
		assertNull( vehicle.getClient() );
	}

	/**
	 * GIVEN: a client with a linked vehicle
	 * WHEN: the returned vehicles set is modified
	 * THEN: the original association is not affected (it must be a copy or a read-only version)
	 */
	@Test
	void testSafeReturn() {
		Associations.Owns.link(client, vehicle);

		Set<Vehicle> vehicles = client.getVehicles();
		vehicles.remove( vehicle );

		assertTrue( vehicles.isEmpty() );

		assertEquals( 1, client.getVehicles().size(), 
			"It must be a copy of the collection or a read-only version"
		);
	}

	/**
	 * GIVEN: a vehicle
	 * WHEN: Associations.Owns.link is called with a null client
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the vehicle and client remain unlinked
	 */
	@Test
	void testLinkNullClient() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Owns.link(null, vehicle)
		);

		assertNull( vehicle.getClient() );
		assertTrue( client.getVehicles().isEmpty() );
	}

	/**
	 * GIVEN: a client
	 * WHEN: Associations.Owns.link is called with a null vehicle
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the client's vehicles remain unchanged
	 */
	@Test
	void testLinkNullVehicle() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Owns.link(client, null)
		);

		assertTrue( client.getVehicles().isEmpty() );
	}

	/**
	 * GIVEN: a vehicle already owned by a client
	 * WHEN: linking it to another client
	 * THEN: an IllegalStateException is thrown
	 * AND: the original association remains unchanged
	 */
	@Test
	void testLinkVehicleWithClient() {
		Client another = new Client(
			new Nif("87654321X"),
			new FullName("Another", "Client")
		);
		Associations.Owns.link(client, vehicle);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Owns.link(another, vehicle)
		);

		assertSame( client, vehicle.getClient() );
		assertTrue( client.getVehicles().contains(vehicle) );
		assertTrue( another.getVehicles().isEmpty() );
	}

	/**
	 * GIVEN: a vehicle present in a client's vehicles but without its client end
	 * WHEN: linking both objects
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testLinkVehicleAlreadyInClient() {
		Vehicle another = new Vehicle(new LicensePlate("5678 KLM"), "seat", "leon");
		// Test-only backdoor: creates an inconsistent inverse association end.
		client._getVehicles().add(another);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Owns.link(client, another)
		);

		assertNull( another.getClient() );
		assertTrue( client.getVehicles().contains(another) );
	}

	/**
	 * GIVEN: no vehicle
	 * WHEN: Associations.Owns.unlink is called
	 * THEN: an IllegalArgumentException is thrown
	 * AND: the vehicle and client remain unlinked
	 */
	@Test
	void testUnlinkNullVehicle() {
		assertThrows(
			IllegalArgumentException.class,
			() -> Associations.Owns.unlink(null)
		);

		assertNull( vehicle.getClient() );
		assertTrue( client.getVehicles().isEmpty() );
	}

	/**
	 * GIVEN: a vehicle without a client
	 * WHEN: Associations.Owns.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the existing association remains unchanged
	 */
	@Test
	void testUnlinkVehicleWithoutClient() {
		assertThrows(
			IllegalStateException.class,
			() -> Associations.Owns.unlink(vehicle)
		);

		assertNull( vehicle.getClient() );
		assertTrue( client.getVehicles().isEmpty() );
	}

	/**
	 * GIVEN: a vehicle whose client does not contain it
	 * WHEN: Associations.Owns.unlink is called
	 * THEN: an IllegalStateException is thrown
	 * AND: the inconsistent association is not modified
	 */
	@Test
	void testUnlinkInconsistentAssociation() {
		// Test-only backdoor: creates an inconsistent association end.
		vehicle._setClient(client);

		assertThrows(
			IllegalStateException.class,
			() -> Associations.Owns.unlink(vehicle)
		);

		assertSame( client, vehicle.getClient() );
		assertFalse( client.getVehicles().contains(vehicle) );
	}
}
