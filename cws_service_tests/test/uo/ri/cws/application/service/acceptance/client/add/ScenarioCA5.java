package uo.ri.cws.application.service.acceptance.client.add;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ScenarioCA5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private ClientDto newClient;

	@Given("[C.A.5] a new non existing client")
	public void givenANewNonExistingClient() {
		newClient = new ClientDtoBuilder().build();
	}

	@When("[C.A.5] I try to add that client with its own nif as recommender")
	public void whenITryToAddThatClientWithItsOwnNifAsRecommender() {
		ctx.tryAndKeep(() -> service.addClient(newClient, newClient.nif));
	}

	@Then("[C.A.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
