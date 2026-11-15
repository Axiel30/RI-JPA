package uo.ri.cws.application.service.acceptance.mechanic.findbynif;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

/**
 * Scenario: [M.FbN.2] Try to find a mechanic with non existing nif
 *   When [M.FbN.2] I try to find a mechanic by a non existent nif
 *   Then [M.FbN.2] mechanic is not found
 */
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.util.exception.BusinessException;

public class ScenarioMFbN2 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private Optional<MechanicDto> result;    

    @When("[M.FbN.2] I try to find a mechanic by a non existent nif")
    public void whenITryToFindMechanicByNonExistentNif() throws BusinessException {
    	result = service.findByNif( NifGenerator.generate() );
    }

    @Then("[M.FbN.2] mechanic is not found")
    public void thenMechanicIsNotFound() {
    	assertTrue( result.isEmpty() );
    }
}
