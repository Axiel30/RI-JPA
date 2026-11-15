package uo.ri.cws.application.service.acceptance.client.update;

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

public class ScenarioCU5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;

	@Given("[C.U.5] a registered client")
	public void givenARegisteredClient() {
		client = DbFixtures.aClient();
	}

	@When("[C.U.5] I try to update a client updated in the while")
	public void whenITryToUpdateAClientUpdatedInTheWhile() {
		ClientDto dto = new ClientDtoBuilder()
				.withId(client.id)
				.withVersion(client.version - 1)
				.withNif(client.nif)
				.build();
		ctx.tryAndKeep(() -> service.updateClient(dto));
	}

	@Then("[C.U.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
