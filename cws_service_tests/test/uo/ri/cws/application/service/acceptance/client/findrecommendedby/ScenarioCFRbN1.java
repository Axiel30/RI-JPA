package uo.ri.cws.application.service.acceptance.client.findrecommendedby;

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

public class ScenarioCFRbN1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord recommender;
	private List<TClientsRecord> expected;
	private List<ClientDto> result;

	@Given("[C.FRbN.1] a registered client with several recommended clients")
	public void givenARegisteredClientWithSeveralRecommendedClients() {
		recommender = DbFixtures.aClient();
		expected = DbFixtures.someClientsRecommendedBy(recommender.id);
	}

	@When("[C.FRbN.1] I search recommended clients by recommender nif")
	public void whenISearchRecommendedClientsByRecommenderNif()
			throws BusinessException {
		result = service.findClientsRecommendedBy(recommender.nif);
	}

	@Then("[C.FRbN.1] I get all recommended clients")
	public void thenIGetAllRecommendedClients() {
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
