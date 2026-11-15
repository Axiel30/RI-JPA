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
 * Scenario: [I.C.5] Trying to create one invoice there is one workorder OPEN
 */
public class ScenarioIC5 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();
    private List<TWorkOrdersRecord> workOrders;
    private List<String> workOrderIds;

    @Given("[I.C.5] a client registered with a vehicle and a list of several finished workorder")
    public void givenClientWithVehicleAndFinishedWorkorders() {
        workOrders = DbFixtures.someWorkOrdersFinishedWithVehicleAndClient();
        workOrderIds = new ArrayList<>(workOrders.stream().map(wo -> wo.id).toList());
    }

    @And("[I.C.5] one OPEN workorder")
    public void andOneOpenWorkorder() {
        String vId = workOrders.get(0).vehicle_Id;
        TWorkOrdersRecord openWorkOrder = DbFixtures.aWorkOrderOpenForVehicle( vId );
        workOrderIds.add(openWorkOrder.id);
    }

    @When("[I.C.5] I try to create an invoice")
    public void whenITryToCreateInvoice() {
        ctx.tryAndKeep(() -> service.create(workOrderIds));
    }

    @Then("[I.C.5] a business error happens with an explaining message")
    public void thenBusinessErrorWithMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}