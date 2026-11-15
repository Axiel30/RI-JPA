package uo.ri.cws.application.service.acceptance.client.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCU1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord original;
	private TClientsRecord loaded;
	private ClientDto updated;

	@Given("[C.U.1] a registered client")
	public void givenARegisteredClient() {
		original = DbFixtures.aClient();
	}

	@When("[C.U.1] I update the client")
	public void whenIUpdateTheClient() throws BusinessException {
		updated = new ClientDtoBuilder()
				.withId(original.id)
				.withVersion(original.version)
				.withNif(original.nif)
				.withName("Updated name")
				.withSurname("Updated surname")
				.withAddressStreet("Updated street")
				.withAddressCity("Updated city")
				.withAddressCountry("Updated country")
				.withAddressZipcode("33001")
				.withPhone("+34600123123")
				.withEmail("updated@example.com")
				.build();
		
		service.updateClient(updated);
	}

	@Then("[C.U.1] the client results updated")
	public void thenTheClientResultsUpdated() {
		loaded = Db.findById(TClientsRecord.class, original.id);
		assertEquals(updated.nif, loaded.nif);
		assertEquals(updated.name, loaded.name);
		assertEquals(updated.surname, loaded.surname);
		assertEquals(updated.addressStreet, loaded.street);
		assertEquals(updated.addressCity, loaded.city);
		assertEquals(updated.addressCountry, loaded.country);
		assertEquals(updated.addressZipcode, loaded.postalCode);
		assertEquals(updated.phone, loaded.phone);
		assertEquals(updated.email, loaded.email);
	}

	@And("[C.U.1] client version increases")
	public void andClientVersionIncreases() {
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals( original.createdAt, loaded.createdAt );
		assertTrue( original.updatedAt.before(loaded.updatedAt) );

	}
}
