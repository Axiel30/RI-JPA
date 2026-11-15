
package uo.ri.cws.application.service.acceptance.mechanic.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;

public class ScenarioMD3 {
	private ExceptionBox ctx = new ExceptionBox();
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private String mechanicId;

	@Given("[M.D.3] a mechanic with work orders registered")
	public void givenAMechanicWithWorkOrdersRegistered() {
		TMechanicsRecord rf = DbFixtures.aMechanicWithWorkOrders();
		mechanicId = rf.id;
	}

	@When("[M.D.3] I try to remove the mechanic")
	public void whenITryToRemoveTheMechanic() {
		ctx.tryAndKeep(() -> service.delete(mechanicId));
	}

	@Then("[M.D.3] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
