
package uo.ri.cws.application.service.acceptance.mechanic.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.MechanicDtoBuilder;
import uo.ri.util.exception.BusinessException;

/**
 * Feature: Add a Mechanic
 * Scenario: [M.A.1] Add a new non existing mechanic
 */
public class ScenarioMA1 {
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private MechanicDto newMechanic;
	private MechanicDto generated;

	@Given("[M.A.1] a new non existing mechanic")
	public void givenNewNonExistingMechanic() throws BusinessException {
		newMechanic = new MechanicDtoBuilder().build();
	}

	@When("[M.A.1] I add a that mechanic")
	public void whenIAddANewNonExistingMechanic() throws BusinessException {
		generated = service.create( newMechanic );
	}

	@Then("[M.A.1] the mechanic results added to the system")
	public void thenTheMechanicResultsAddedToTheSystem() {
		// checks on the returned object
		assertNotNull( generated.id );
		assertFalse( generated.id.isEmpty() );
		assertEquals( 1L, generated.version );
		
		assertEquals( newMechanic.name, generated.name );
		assertEquals( newMechanic.surname, generated.surname );
		assertEquals( newMechanic.nif, generated.nif );

		// checks on the database
		TMechanicsRecord loaded = Db.findById(TMechanicsRecord.class, generated.id);
		Asserts.recordMatchesDto(generated, loaded);
		Asserts.isNow( loaded.createdAt );
		Asserts.isNow( loaded.updatedAt );
	}
}
