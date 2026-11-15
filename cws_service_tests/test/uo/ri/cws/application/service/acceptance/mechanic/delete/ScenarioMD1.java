
package uo.ri.cws.application.service.acceptance.mechanic.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

public class ScenarioMD1 {
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private String mechanicId;

	@Given("[M.D.1] a registered mechanic")
	public void givenAMechanic() {
		TMechanicsRecord mechanic = DbFixtures.aMechanic();
		mechanicId = mechanic.id;
	}

	@When("[M.D.1] I remove the mechanic")
	public void whenIRemoveTheMechanic() throws BusinessException {
		service.delete(mechanicId);
	}

	@Then("[M.D.1] the mechanic no longer exists")
	public void thenTheMechanicNoLongerExists() {
		TMechanicsRecord r = Db.findById(TMechanicsRecord.class, mechanicId);
		assertNull(r);
	}
}
