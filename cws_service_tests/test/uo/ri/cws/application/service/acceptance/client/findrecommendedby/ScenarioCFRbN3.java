package uo.ri.cws.application.service.acceptance.client.findrecommendedby;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCFRbN3 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private List<ClientDto> result;

	@When("[C.FRbN.3] I search recommended clients for a non existing recommender nif")
	public void whenISearchRecommendedClientsForANonExistingRecommenderNif()
			throws BusinessException {
		result = service.findClientsRecommendedBy(NifGenerator.generate());
	}

	@Then("[C.FRbN.3] I get an empty list")
	public void thenIGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
