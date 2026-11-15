package uo.ri.cws.application.service.acceptance.mechanic.findall;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import io.cucumber.datatable.DataTable;
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

public class ScenarioMFA1 {
    private MechanicCrudService service = Factories.service.forMechanicCrudService();
    private List<TMechanicsRecord> expected = new ArrayList<>();
    private List<MechanicDto> result;

    @Given("[M.FA.1] the following relation of mechanics")
    public void givenTheFollowingRelationOfMechanics(DataTable dataTable) {
		List<List<String>> rows = dataTable.asLists(String.class);
		for (int i = 1; i < rows.size(); i++) {
		    List<String> columns = rows.get(i);
		    
			String nif = columns.get(0);
			String name = columns.get(1);
			String surname = columns.get(2);
			
			TMechanicsRecord m = DbFixtures.aMechanicOf( nif, name, surname );
			expected.add( m );
		}
    }

    @When("[M.FA.1] we read all mechanics")
    public void whenWeReadAllMechanics() throws BusinessException {
        result = service.findAll();
    }

    @Then("[M.FA.1] we get all of them")
    public void thenWeGetTheFollowingListOfMechanics() {
		for (TMechanicsRecord m : expected) {
			MechanicDto found = find( m.id );
			assertNotNull( found );
			Asserts.dtoMatchesRecord( m, found );
		}
    }

	private MechanicDto find(String mId) {
		for (MechanicDto dto : result) {
			if (dto.id.equals( mId )) {
				return dto;
			}
		}
		return null;
	}
}
