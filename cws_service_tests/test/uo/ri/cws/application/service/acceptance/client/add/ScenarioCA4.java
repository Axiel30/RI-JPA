package uo.ri.cws.application.service.acceptance.client.add;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ScenarioCA4 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private ClientDto newClient;

	@Given("[C.A.4] a new non existing client")
	public void givenANewNonExistingClient() {
		newClient = new ClientDtoBuilder().build();
	}

	@When("[C.A.4] I try to add that client with a non existing recommender nif")
	public void whenITryToAddThatClientWithANonExistingRecommenderNif() {
		ctx.tryAndKeep(
				() -> service.addClient(newClient, NifGenerator.generate())
			);
	}

	@Then("[C.A.4] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
