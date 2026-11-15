package uo.ri.cws.application.service.acceptance.client.findbynif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCFbN1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;
	private Optional<ClientDto> result;

	@Given("[C.FbN.1] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[C.FbN.1] I search the client by nif")
	public void whenISearchTheClientByNif() throws BusinessException {
		result = service.findClientByNif(client.nif);
	}

	@Then("[C.FbN.1] I get the client")
	public void thenIGetTheClient() {
		assertTrue(result.isPresent());
		Asserts.dtoMatchesRecord(client, result.get());
	}
}
