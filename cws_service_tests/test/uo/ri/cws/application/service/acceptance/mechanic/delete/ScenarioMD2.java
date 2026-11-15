
package uo.ri.cws.application.service.acceptance.mechanic.delete;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;

public class ScenarioMD2 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();

	@When("[M.D.2] I try to remove a non existent mechanic")
	public void whenITryToRemoveANonExistentMechanic() {
		ctx.tryAndKeep(
				() -> service.delete("non-existent-id")
			);
	}

	@Then("[M.D.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
