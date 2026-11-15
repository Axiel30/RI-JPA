package uo.ri.cws.application.service.acceptance.mechanic.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TMechanicsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.MechanicDtoBuilder;
import uo.ri.util.exception.BusinessException;

public class ScenarioMU1 {
	private MechanicCrudService service = Factories.service.forMechanicCrudService();
	private TMechanicsRecord original;
    private TMechanicsRecord loaded;

	@Given("[M.U.1] a mechanic")
	public void givenAMechanic() {
		original = DbFixtures.aMechanic();
	}

	@When("[M.U.1] I update the mechanic")
	public void whenIUpdateTheMechanic() throws BusinessException {
		MechanicDto dto = new MechanicDtoBuilder()
                        .withId(  original.id )
                        .withName( "UpdatedName" )
                        .withSurname( "UpdatedSurname" )
                        .withNif( "12345678Z" ) 
                        .withVersion( original.version )
                        .build();

		service.update(dto);
	}

	@Then("[M.U.1] the mechanic results updated")
	public void thenTheMechanicResultsUpdatedForFieldsNameAndSurname() {
		loaded = Db.findById(TMechanicsRecord.class, original.id);
		
		assertEquals("UpdatedName", loaded.name);
		assertEquals("UpdatedSurname", loaded.surname);
		assertEquals("12345678Z", loaded.nif); 
	}

	@And("[M.U.1] mechanic version increases")
	public void andMechanicVersionIncreases() {
		assertTrue( original.version < loaded.version, "Version must increase" );
		assertEquals( original.createdAt, loaded.createdAt );
		assertTrue( original.updatedAt.before(loaded.updatedAt) );
	}
}
