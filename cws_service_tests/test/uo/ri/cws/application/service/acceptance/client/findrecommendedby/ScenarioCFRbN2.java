package uo.ri.cws.application.service.acceptance.client.findrecommendedby;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCFRbN2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord recommender;
	private List<ClientDto> result;

	@Given("[C.FRbN.2] a registered client")
	public void givenARegisteredClient() {
		recommender = DbFixtures.aClient();
	}

	@When("[C.FRbN.2] I search recommended clients by recommender nif")
	public void whenISearchRecommendedClientsByRecommenderNif()
			throws BusinessException {
		result = service.findClientsRecommendedBy(recommender.nif);
	}

	@Then("[C.FRbN.2] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
