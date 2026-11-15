
package uo.ri.cws.application.service.acceptance.mechanic.add;

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

public class ScenarioMA2 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private String mechanicNif;

	@Given("[M.A.2] a registered mechanic")
	public void givenAMechanic() {
		TMechanicsRecord mechanic = DbFixtures.aMechanic();
		mechanicNif = mechanic.nif;
	}

	@When("[M.A.2] I try to add a new mechanic with same nif")
	public void whenITryToAddANewMechanicWithSameNif() {
		MechanicDto dto = new MechanicDtoBuilder().withNif(mechanicNif).build();
				
		ctx.tryAndKeep(() -> service.create(dto));
	}

	@Then("[M.A.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}

}
