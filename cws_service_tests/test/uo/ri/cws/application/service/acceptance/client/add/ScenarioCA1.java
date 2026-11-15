package uo.ri.cws.application.service.acceptance.client.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCA1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private ClientDto newClient;
	private ClientDto generated;

	@Given("[C.A.1] a new non existing client")
	public void givenANewNonExistingClient() {
		newClient = new ClientDtoBuilder().build();
	}

	@When("[C.A.1] I add that client")
	public void whenIAddThatClient() throws BusinessException {
		generated = service.addClient(newClient, null);
	}

	@Then("[C.A.1] the client results added to the system")
	public void thenTheClientResultsAddedToTheSystem() {
		assertNotNull(generated.id);
		assertFalse(generated.id.isBlank());
		assertEquals(1L, generated.version);
		Asserts.matches(newClient, generated);

		TClientsRecord loaded = Db.findById(TClientsRecord.class, generated.id);
		Asserts.recordMatchesDto(generated, loaded);
		Asserts.isNow(loaded.createdAt);
		Asserts.isNow(loaded.updatedAt);
	}

	@And("[C.A.1] the client has a cash payment mean")
	public void andTheClientHasACashPaymentMean() {
		List<TPaymentMeansRecord> paymentMeans = Db.findAllBy(TPaymentMeansRecord.class, "CLIENT_ID", generated.id);
		assertEquals(1, paymentMeans.size());
		assertEquals("Cash", paymentMeans.get(0).dtype);
		assertNotNull(Db.findById(TCashesRecord.class, paymentMeans.get(0).id));
	}
}
