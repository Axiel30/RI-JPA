package uo.ri.ui.cashier.paymentmean.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeansByClientNifAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String nif = Console.readString("Client nif");

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		List<PaymentMeanDto> means = service.findPaymentMeansByClientNif(nif);
		if (means.isEmpty()) {
			Console.println("There are no payment means for the client");
			return;
		}

		Printer.printPaymentMeans(means);
	}

}
