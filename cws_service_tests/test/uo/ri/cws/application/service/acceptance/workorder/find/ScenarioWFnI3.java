package uo.ri.cws.application.service.acceptance.workorder.find;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.And;
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
 * Scenario: [W.FnI.3] Find not invoiced workorders by client nif with some not finished workorders
 */
public class ScenarioWFnI3 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private List<TWorkOrdersRecord> approvedWorkOrders;
	private TClientsRecord client;
	private String vehicleId;
	private List<InvoicingWorkOrderDto> found;

	@Given("[W.FnI.3] a client registered with a vehicle and a list of several approved workorders")
    public void givenClientWithVehicleAndSeveralApprovedWorkorders() {
		client = DbFixtures.aClient();
    	approvedWorkOrders = DbFixtures.someWorkOrdersApprovedWithVehicleForClient(client.id);
    	// Get vehicle ID from the first approved workorder
    	vehicleId = approvedWorkOrders.get(0).vehicle_Id;
    }

    @And("[W.FnI.3] one INVOICED workorder")
    public void andOneInvoicedWorkorder() {
    	DbFixtures.aWorkOrderInvoicedForVehicle(vehicleId);
    }

    @And("[W.FnI.3] one OPEN workorder")
    public void andOneOpenWorkorder() {
    	DbFixtures.aWorkOrderOpenForVehicle(vehicleId);
    }

    @And("[W.FnI.3] one ASSIGNED workorder")
    public void andOneAssignedWorkorder() {
    	DbFixtures.aWorkOrderAssignedForVehicle(vehicleId);
    }
    
    @And("[W.FnI.3] one FINISHED workorder")
    public void andOneFinishedWorkorder() {
    	DbFixtures.aWorkOrderFinishedForVehicle(vehicleId);
    }

    @When("[W.FnI.3] I search not invoiced workorders by client nif")
    public void whenISearchNotInvoicedWorkordersByClientNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(client.nif);
    }

    @Then("[W.FnI.3] I get only approved workorders")
    public void thenIGetOnlyApprovedWorkorders() {
    	assertEquals(approvedWorkOrders.size(), found.size());
    	found.forEach(wo -> assertEquals("APPROVED", wo.state));
    }
}