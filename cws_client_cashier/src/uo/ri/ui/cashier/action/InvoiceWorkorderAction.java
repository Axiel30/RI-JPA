package uo.ri.ui.cashier.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {

	@Override
	public void execute() throws BusinessException {
		List<String> workOrderIds = new ArrayList<>();

		// Ask the user the work order ids
		do {
			String id = Console.readString("Workorder id");
			workOrderIds.add(id);
		} while ( moreWorkOrders() );

		InvoicingService cs = Factories.service.forCreateInvoiceService();
		InvoiceDto invoice = cs.create(workOrderIds);

		Printer.printInvoice( invoice );
	}

	private boolean moreWorkOrders() {
		return Console
				.readString("More work orders? (y/n)")
				.equalsIgnoreCase("y");
	}

}
