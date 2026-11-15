package uo.ri.cws.application.service.acceptance.invoice.create;

import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;

/**
 * Scenario: [I.C.6] Trying to create one invoice but one workorder is INVOICED
 */
public class ScenarioIC6 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();
    private List<TWorkOrdersRecord> workOrders;
    private List<String> workOrderIds;

    @Given("[I.C.6] a client registered with a vehicle and a list of several finished workorder")
    public void givenClientWithVehicleAndFinishedWorkorders() {
        workOrders = DbFixtures.someWorkOrdersFinishedWithVehicleAndClient();
        workOrderIds = new ArrayList<>( listOfIds() );
    }

	private List<String> listOfIds() {
		return workOrders.stream().map(wo -> wo.id).toList();
	}

    @And("[I.C.6] one INVOICED workorder")
    public void andOneInvoicedWorkorder() {
        String vId = workOrders.get(0).vehicle_Id;
        TWorkOrdersRecord newWorkOrder = DbFixtures.aWorkOrderInvoicedForVehicle( vId );
        workOrderIds.add(newWorkOrder.id);
    }

    @When("[I.C.6] I try to create an invoice")
    public void whenITryToCreateInvoice() {
        ctx.tryAndKeep(() -> service.create(workOrderIds));
    }

    @Then("[I.C.6] a business error happens with an explaining message")
    public void thenBusinessErrorWithMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}