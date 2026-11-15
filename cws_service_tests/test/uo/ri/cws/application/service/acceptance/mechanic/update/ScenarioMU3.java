package uo.ri.cws.application.service.acceptance.mechanic.update;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.MechanicDtoBuilder;

public class ScenarioMU3 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private TMechanicsRecord mechanic;

	@Given("[M.U.3] a mechanic")
	public void givenAMechanic() {
		mechanic = DbFixtures.aMechanic();
	}

	@When("[M.U.3] I try to update a mechanic updated in the while")
	public void whenITryToUpdateAMechanicUpdatedInTheWhile() {
		MechanicDto dto = new MechanicDtoBuilder()
                        .withId(mechanic.id)
                        .withName("UpdatedName")
                        .withVersion(mechanic.version - 1) // simulates old version
                        .build();

		ctx.tryAndKeep(() -> service.update(dto));
	}

	@Then("[M.U.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
