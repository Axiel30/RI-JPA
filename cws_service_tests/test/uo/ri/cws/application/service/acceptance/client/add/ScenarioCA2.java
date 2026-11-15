package uo.ri.cws.application.service.acceptance.client.add;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.ClientDtoBuilder;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioCA2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord recommender;
	private ClientDto newClient;
	private ClientDto generated;

	@Given("[C.A.2] a registered recommender client")
	public void givenARegisteredRecommenderClient() {
		recommender = DbFixtures.aClient();
	}

	@And("[C.A.2] a new non existing client")
	public void andANewNonExistingClient() {
		newClient = new ClientDtoBuilder().build();
	}

	@When("[C.A.2] I add that client with the recommender nif")
	public void whenIAddThatClientWithTheRecommenderNif() throws BusinessException {
		generated = service.addClient(newClient, recommender.nif);
	}

	@Then("[C.A.2] the client results added to the system")
	public void thenTheClientResultsAddedToTheSystem() {
		assertNotNull(generated.id);
		Asserts.matches(newClient, generated);

		TClientsRecord recorded = Db.findById(TClientsRecord.class, generated.id);
		Asserts.recordMatchesDto(generated, recorded);
	}

	@And("[C.A.2] the client has a cash payment mean")
	public void andTheClientHasACashPaymentMean() {
		List<TPaymentMeansRecord> paymentMeans = Db.findAllBy(TPaymentMeansRecord.class, "CLIENT_ID", generated.id);
		assertEquals(1, paymentMeans.size());
		assertNotNull(Db.findById(TCashesRecord.class, paymentMeans.get(0).id));
	}

	@And("[C.A.2] the recommendation results registered")
	public void andTheRecommendationResultsRegistered() {
		TRecommendationsRecord recommendation = Db.findBy(TRecommendationsRecord.class, "RECOMMENDED_ID", generated.id);
		assertNotNull(recommendation);
		assertEquals(recommender.id, recommendation.recommender_Id);
	}
}
