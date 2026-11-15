package uo.ri.cws.application.service.acceptance.client.delete;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TCashesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TPaymentMeansRecord;
import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.util.exception.BusinessException;

public class ScenarioCD1 {
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;
	private List<TPaymentMeansRecord> paymentMeans;

	@Given("[C.D.1] a registered client with cash")
	public void givenARegisteredClientWithCash() {
		client = DbFixtures.aClientWithCash();
		paymentMeans = Db.findAllBy(TPaymentMeansRecord.class, "CLIENT_ID", client.id);
	}

	@When("[C.D.1] I remove the client by nif")
	public void whenIRemoveTheClientByNif() throws BusinessException {
		service.deleteClientByNif(client.nif);
	}

	@Then("[C.D.1] the client no longer exists")
	public void thenTheClientNoLongerExists() {
		assertNull(Db.findById(TClientsRecord.class, client.id));
	}

	@And("[C.D.1] the client payment means are deleted")
	public void andTheClientPaymentMeansAreDeleted() {
		for (TPaymentMeansRecord paymentMean : paymentMeans) {
			assertNull(Db.findById(TPaymentMeansRecord.class, paymentMean.id));
			assertNull(Db.findById(TCashesRecord.class, paymentMean.id));
		}
	}
}
