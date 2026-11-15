package uo.ri.cws.application.service.acceptance.client.findall;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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

public class ScenarioCFA1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private List<TClientsRecord> expected;
	private List<ClientDto> result;

	@Given("[C.FA.1] a relation of clients")
	public void givenARelationOfClients() {
		expected = DbFixtures.someClients();
	}

	@When("[C.FA.1] we read all clients")
	public void whenWeReadAllClients() throws BusinessException {
		result = service.findAllClients();
	}

	@Then("[C.FA.1] we get all of them")
	public void thenWeGetAllOfThem() {
		assertEquals(expected.size(), result.size());
		for (TClientsRecord client : expected) {
			ClientDto found = find(client.id);
			assertNotNull(found);
			Asserts.dtoMatchesRecord(client, found);
		}
	}

	private ClientDto find(String id) {
		return result.stream()
				.filter(dto -> id.equals(dto.id))
				.findFirst()
				.orElse(null);
	}
}
