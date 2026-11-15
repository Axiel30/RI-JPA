package uo.ri.cws.application.service.acceptance.client.findall;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCFA2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private List<ClientDto> result;

	@Given("[C.FA.2] there are no clients")
	public void givenThereAreNoClients() {
	}

	@When("[C.FA.2] we read all clients")
	public void whenWeReadAllClients() throws BusinessException {
		result = service.findAllClients();
	}

	@Then("[C.FA.2] we get an empty list")
	public void thenWeGetAnEmptyList() {
		assertTrue(result.isEmpty());
	}
}
