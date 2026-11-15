package uo.ri.cws.application.service.acceptance.client.findbynif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCFbN2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private Optional<ClientDto> result;

	@When("[C.FbN.2] I search a non existing client by nif")
	public void whenISearchANonExistingClientByNif() throws BusinessException {
		result = service.findClientByNif(NifGenerator.generate());
	}

	@Then("[C.FbN.2] no client is found")
	public void thenNoClientIsFound() {
		assertTrue(result.isEmpty());
	}
}
