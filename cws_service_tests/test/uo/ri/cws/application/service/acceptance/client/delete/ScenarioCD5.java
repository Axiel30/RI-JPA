package uo.ri.cws.application.service.acceptance.client.delete;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.client.ClientCrudService;

public class ScenarioCD5 {
	private ExceptionBox ctx = new ExceptionBox();
	private ClientCrudService service = Factories.service.forClientCrudService();
	private TClientsRecord client;

	@Given("[C.D.5] a registered client with paid invoices")
	public void givenARegisteredClientWithPaidInvoices() {
		client = DbFixtures.aClientWithPaidInvoice();
	}

	@When("[C.D.5] I try to remove the client by nif")
	public void whenITryToRemoveTheClientByNif() {
		ctx.tryAndKeep(() -> service.deleteClientByNif(client.nif));
	}

	@Then("[C.D.5] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		ctx.assertBusinessExceptionWithMessage();
	}
}
