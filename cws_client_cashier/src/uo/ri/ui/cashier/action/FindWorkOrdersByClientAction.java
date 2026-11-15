package uo.ri.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindWorkOrdersByClientAction implements Action {

	@Override
	public void execute() throws BusinessException {
		InvoicingService cs = Factories.service.forCreateInvoiceService();

		String nif = Console.readString("Client nif");

		List<InvoicingWorkOrderDto> reps = cs.findWorkOrdersByClientNif( nif );

		if (reps.size() == 0) {
			Console.println("There are no work orders");
			return;
		}

		Console.println("\nWork orders of the client\n");
		for(InvoicingWorkOrderDto rep : reps) {
			Printer.printWorkOrder( rep );
		}
	}

}
