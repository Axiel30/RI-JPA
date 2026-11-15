package uo.ri.cws.application.service.acceptance.workorder.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [W.FnI.1] Find not invoiced workorders by client nif
 */
public class ScenarioWFnI1 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private List<TWorkOrdersRecord> workOrders;
	private TClientsRecord client;
	private List<InvoicingWorkOrderDto> found;

	@Given("[W.FnI.1] a client registered with a vehicle and a list of several approved workorders")
    public void givenClientWithVehicleAndSeveralFinishedWorkorders() {
		client = DbFixtures.aClient();
    	workOrders = DbFixtures.someWorkOrdersApprovedWithVehicleForClient( client.id );
   }

    @When("[W.FnI.1] I search not invoiced workorders by client nif")
    public void whenISearchNotInvoicedWorkordersByClientNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(client.nif);
    }

    @Then("[W.FnI.1] I get only approved workorders")
    public void thenIGetOnlyFinishedWorkorders() {
    	assertEquals( workOrders.size(), found.size() );
    	found.forEach( wo -> assertEquals("APPROVED", wo.state) );
    }

}