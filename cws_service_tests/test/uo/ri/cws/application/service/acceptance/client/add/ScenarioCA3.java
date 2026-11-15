package uo.ri.cws.application.service.acceptance.client.add;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ScenarioCA3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private String clientNif;

	@Given("[C.A.3] a registered client")
	public void givenARegisteredClient() {
		TClientsRecord client = DbFixtures.aClient();
		clientNif = client.nif;
	}

	@When("[C.A.3] I try to add a new client with same nif")
	public void whenITryToAddANewClientWithSameNif() {
		ClientDto dto = new ClientDtoBuilder().withNif(clientNif).build();
		ctx.tryAndKeep(() -> service.addClient(dto, null));
	}

	@Then("[C.A.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
