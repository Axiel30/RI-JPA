package uo.ri.ui.cashier.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindWorkOrdersByPlateAction implements Action {

	@Override
	public void execute() throws BusinessException {
		InvoicingService service = Factories.service.forCreateInvoiceService();

		String plate = Console.readString("Plate number");

		List<InvoicingWorkOrderDto> workOrders = service.findWorkOrdersByPlateNumber(plate);

		Console.println("\nWork orders for the vehicle\n");
		workOrders.forEach(wo -> Printer.printWorkOrder(wo));

		if (workOrders.isEmpty()) {
			Console.println("There are no work orders for the vehicle");
		}
	}

}
