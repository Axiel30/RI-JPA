package uo.ri.cws.application.service.acceptance.invoice.create;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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
 * Scenario: [I.C.1] Create one invoice for an existing workorder
 */
public class ScenarioIC1 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private TWorkOrdersRecord workOrder;
	private InvoiceDto expectedValues;
	private InvoiceDto generated;

	@Given("[I.C.1] a client registered with a vehicle and one approved workorder")
    public void givenClientWithVehicleAndApprovedWorkorder() {
    	workOrder = DbFixtures.aClientWithVehicleAndOneApprovedWorkorder();
		expectedValues = InvoiceHelper.computeFor(workOrder);
    }

    @When("[I.C.1] I create an invoice for the workorders")
    public void whenICreateInvoiceForWorkorders() throws BusinessException {
    	generated = service.create( List.of(workOrder.id) );
    }

    @Then("[I.C.1] an invoice is created")
    public void thenInvoiceIsCreated() {
    	// checks on the returned object
    	assertNotNull(generated.id);
    	assertFalse(generated.id.isEmpty());
    	assertEquals(1, generated.version);
		Asserts.rightAmounts(expectedValues, generated);
    	
		// checks on the database
		TInvoicesRecord loaded = Db.findById(TInvoicesRecord.class, generated.id);
		Asserts.recordMatchesDto(generated, loaded);
	}

    @Then("[I.C.1] the workorder is marked as INVOICED")
    public void thenWorkOrderIsMarkedAsInvoiced() {
    	TWorkOrdersRecord wo = Db.findById(TWorkOrdersRecord.class, workOrder.id);
    	assertEquals("INVOICED", wo.state);
    }
    
    @Then("[I.C.1] the workorder is aggregated to the invoice")
    public void iCTheWorkorderIsAggregatedToTheInvoice() {
    	TWorkOrdersRecord wo = Db.findById(TWorkOrdersRecord.class, workOrder.id);
    	assertEquals("INVOICED", wo.state);

    }

}