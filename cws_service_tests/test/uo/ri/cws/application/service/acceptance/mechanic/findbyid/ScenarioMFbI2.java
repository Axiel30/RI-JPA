package uo.ri.cws.application.service.acceptance.mechanic.findbyid;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioMFbI2 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private Optional<MechanicDto> result;

    @When("[M.FbI.2] I try to find a non existent mechanic")
    public void whenITryToFindNonExistentMechanic() throws BusinessException {
        result = service.findById( "NON_EXISTENT_ID" );
    }

    @Then("[M.FbI.2] mechanic is not found")
    public void thenMechanicIsNotFound() {
        assertTrue( result.isEmpty() );
    }
}
