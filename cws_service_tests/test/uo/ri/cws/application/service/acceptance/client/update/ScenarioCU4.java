package uo.ri.cws.application.service.acceptance.client.update;

import java.util.List;

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

public class ScenarioCU4 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;
	private TClientsRecord otherClient;

	@Given("[C.U.4] two registered clients")
	public void givenTwoRegisteredClients() {
		List<TClientsRecord> clients = DbFixtures.someClients();
		client = clients.get(0);
		otherClient = clients.get(1);
	}

	@When("[C.U.4] I try to update one client with the other client nif")
	public void whenITryToUpdateOneClientWithTheOtherClientNif() {
		ClientDto dto = new ClientDtoBuilder()
				.withId(client.id)
				.withVersion(client.version)
				.withNif(otherClient.nif)
				.build();
		ctx.tryAndKeep(() -> service.updateClient(dto));
	}

	@Then("[C.U.4] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
