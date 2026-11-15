package uo.ri.cws.application.service.acceptance.client.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TRecommendationsRecord;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.util.exception.BusinessException;

public class ScenarioCD2 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;
	private List<TRecommendationsRecord> recommendations = new ArrayList<>();

	@Given("[C.D.2] a registered client with made and received recommendations")
	public void givenARegisteredClientWithMadeAndReceivedRecommendations() {
		client = DbFixtures.aClientWithMadeAndReceivedRecommendations();
		recommendations.addAll(Db.findAllBy(TRecommendationsRecord.class, "RECOMMENDER_ID", client.id));
		recommendations.addAll(Db.findAllBy(TRecommendationsRecord.class, "RECOMMENDED_ID", client.id));
	}

	@When("[C.D.2] I remove the client by nif")
	public void whenIRemoveTheClientByNif() throws BusinessException {
		service.deleteClientByNif(client.nif);
	}

	@Then("[C.D.2] the client no longer exists")
	public void thenTheClientNoLongerExists() {
		assertNull(Db.findById(TClientsRecord.class, client.id));
	}

	@And("[C.D.2] the client recommendations are deleted")
	public void andTheClientRecommendationsAreDeleted() {
		for (TRecommendationsRecord recommendation : recommendations) {
			assertNull(Db.findById(TRecommendationsRecord.class, recommendation.id));
		}
	}
}
