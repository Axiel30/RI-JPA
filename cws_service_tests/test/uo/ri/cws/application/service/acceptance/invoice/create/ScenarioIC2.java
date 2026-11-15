package uo.ri.cws.application.service.acceptance.invoice.create;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.acceptance.util.Asserts;
import uo.ri.cws.application.service.acceptance.util.dbfixture.Db;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TInvoicesRecord;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TWorkOrdersRecord;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [I.C.2] Create one invoice for multiple existing workorders
 */
public class ScenarioIC2 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

    private List<TWorkOrdersRecord> workOrders;
	private InvoiceDto invoice;


	@Given("[I.C.2] a client registered with a vehicle and a list of several approved workorders")
    public void givenClientWithVehicleAndFinishedWorkorders() {
    	workOrders = DbFixtures.someWorkOrdersApprovedWithVehicleAndClient();
    }

    @When("[I.C.2] I create an invoice for the workorders")
    public void whenICreateInvoiceForWorkorders() throws BusinessException {
    	invoice = service.create( listOfWorkOrderIds() );
    }

	@Then("[I.C.2] an invoice is created")
    public void thenInvoiceIsCreated() {
		InvoiceDto expectedValues = InvoiceHelper.computeFor(workOrders);
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, invoice.id);
		
		Asserts.rightAmounts(expectedValues, invoice);
		Asserts.recordMatchesDto(invoice, loaded);
    }
    
    @And("[I.C.2] the workorders are marked as INVOICED")
    public void thenWorkOrdersAreMarkedAsInvoiced() {
    	for(String id : listOfWorkOrderIds()) {
			TWorkOrdersRecord wo = Db.findById(TWorkOrdersRecord.class, id);
			assertEquals("INVOICED", wo.state);
		}
    }

    private List<String> listOfWorkOrderIds() {
		return workOrders.stream().map(wo -> wo.id).toList();
	}

}
