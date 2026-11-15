package uo.ri.cws.application.service.acceptance.invoice.create;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;

/**
 * Scenario: [I.C.3] Trying to create one invoice there is one non existing workorder
 */
public class ScenarioIC3 {
    private InvoicingService service = Factories.service.forCreateInvoiceService();
    private ExceptionBox ctx = new ExceptionBox();
    private List<TWorkOrdersRecord> workOrders;

    @Given("[I.C.3] a client registered with a vehicle and a list of several finished workorders")
    public void givenClientWithVehicleAndFinishedWorkorders() {
        workOrders = DbFixtures.someWorkOrdersFinishedWithVehicleAndClient();
    }

    @When("[I.C.3] I try to create an invoice including one non existent workorder id")
    public void whenITryToCreateInvoiceWithNonExistentWorkorderId() {
        List<String> ids = listOfWorkOrderIds();
        List<String> idsWithNonExistent = List.of(ids.get(0), "NON_EXISTENT_ID");
        
        ctx.tryAndKeep(() -> service.create(idsWithNonExistent));
    }

	private List<String> listOfWorkOrderIds() {
		return workOrders.stream().map(wo -> wo.id).toList();
	}

    @Then("[I.C.3] a business error happens with an explaining message")
    public void thenBusinessErrorWithMessage() {
        ctx.assertBusinessExceptionWithMessage();
    }
}