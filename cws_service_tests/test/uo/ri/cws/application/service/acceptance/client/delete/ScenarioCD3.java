package uo.ri.cws.application.service.acceptance.client.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.cws.application.service.client.ClientCrudService;

public class ScenarioCD3 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();

	@When("[C.D.3] I try to remove a non existing client by nif")
	public void whenITryToRemoveANonExistingClientByNif() {
		ctx.tryAndKeep(
				() -> service.deleteClientByNif(NifGenerator.generate())
			);
	}

	@Then("[C.D.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
