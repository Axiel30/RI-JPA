package uo.ri.cws.application.service.acceptance.mechanic.update;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.MechanicDtoBuilder;

public class ScenarioMU2 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();

	@When("[M.U.2] I try to update a non existing mechanic")
	public void whenITryToUpdateANonExistingMechanic() {
		MechanicDto dto = new MechanicDtoBuilder()
                        .withId("non-existent-id")
                        .build();
                        
		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[M.U.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
