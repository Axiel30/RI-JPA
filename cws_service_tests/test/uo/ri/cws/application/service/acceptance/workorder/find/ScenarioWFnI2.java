package uo.ri.cws.application.service.acceptance.workorder.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [W.FnI.2] Find not invoiced workorders by client nif with no workorders
 */
public class ScenarioWFnI2 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private TClientsRecord client;
	private List<InvoicingWorkOrderDto> found;

	@Given("[W.FnI.2] a client registered")
    public void givenClientRegistered() {
		client = DbFixtures.aClient();
    }

    @When("[W.FnI.2] I search not invoiced workorders by client nif")
    public void whenISearchNotInvoicedWorkordersByClientNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(client.nif);
    }

    @Then("[W.FnI.2] I get an empty list")
    public void thenIGetAnEmptyList() {
    	assertTrue(found.isEmpty());
    }
}