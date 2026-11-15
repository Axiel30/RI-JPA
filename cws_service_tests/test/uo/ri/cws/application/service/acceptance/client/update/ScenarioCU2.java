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
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCU2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord original;
	private TClientsRecord loaded;
	private String newNif;

	@Given("[C.U.2] a registered client")
	public void givenARegisteredClient() {
		original = DbFixtures.aClient();
	}

	@When("[C.U.2] I update the client with a new nif")
	public void whenIUpdateTheClientWithANewNif() throws BusinessException {
		newNif = NifGenerator.generate();
		ClientDto dto = new ClientDtoBuilder()
				.withId(original.id)
				.withVersion(original.version)
				.withNif(newNif)
				.withName(original.name)
				.withSurname(original.surname)
				.withAddressStreet(original.street)
				.withAddressCity(original.city)
				.withAddressCountry(original.country)
				.withAddressZipcode(original.postalCode)
				.withPhone(original.phone)
				.withEmail(original.email)
				.build();
		service.updateClient(dto);
	}

	@Then("[C.U.2] the client results updated")
	public void thenTheClientResultsUpdated() {
		loaded = Db.findById(TClientsRecord.class, original.id);
		assertEquals(newNif, loaded.nif);
		assertEquals(original.name, loaded.name);
		assertEquals(original.surname, loaded.surname);
	}

	@And("[C.U.2] client version increases")
	public void andClientVersionIncreases() {
		assertTrue(original.version < loaded.version, "Version must increase");
		assertEquals(original.createdAt, loaded.createdAt);
	}
}
