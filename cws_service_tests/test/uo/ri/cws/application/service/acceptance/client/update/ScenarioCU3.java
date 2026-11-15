package uo.ri.cws.application.service.acceptance.client.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;

public class ScenarioCU3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();

	@When("[C.U.3] I try to update a non existing client")
	public void whenITryToUpdateANonExistingClient() {
		ClientDto dto = new ClientDtoBuilder()
				.withId("non-existing-id")
				.withVersion(1L)
				.build();
		ctx.tryAndKeep(() -> service.updateClient(dto));
	}

	@Then("[C.U.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
