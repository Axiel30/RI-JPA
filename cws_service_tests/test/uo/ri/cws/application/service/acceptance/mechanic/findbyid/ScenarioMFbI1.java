package uo.ri.cws.application.service.acceptance.mechanic.findbyid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.util.exception.BusinessException;

public class ScenarioMFbI1 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private TMechanicsRecord mechanic;
    private Optional<MechanicDto> result;

    @Given("[M.FbI.1] a mechanic")
    public void givenAMechanic() {
        mechanic = DbFixtures.aMechanic();
    }

    @When("[M.FbI.1] I look for mechanic")
    public void whenILookForMechanic() throws BusinessException {
        result = service.findById(mechanic.id);
    }

    @Then("[M.FbI.1] I get mechanic")
    public void thenIGetMechanic() {
    	assertTrue(result.isPresent());
    	Asserts.dtoMatchesRecord(mechanic, result.get());
    }
}
