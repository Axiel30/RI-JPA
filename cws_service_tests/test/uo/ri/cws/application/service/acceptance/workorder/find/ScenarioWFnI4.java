package uo.ri.cws.application.service.acceptance.workorder.find;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.NifGenerator;
import uo.ri.util.exception.BusinessException;

/**
 * Scenario: [W.FnI.4] Try to find workorders for a non existent client
 */
public class ScenarioWFnI4 {
	private InvoicingService service = Factories.service.forCreateInvoiceService();

	private List<InvoicingWorkOrderDto> found;

	@When("[W.FnI.4] I search workorders for a non existent nif")
    public void whenISearchWorkordersForNonExistentNif() throws BusinessException {
    	found = service.findNotInvoicedWorkOrdersByClientNif(NifGenerator.generate());
    }

    @Then("[W.FnI.4] I get an empty list")
    public void thenIGetAnEmptyList() {
    	assertTrue( found.isEmpty() );
    }
}