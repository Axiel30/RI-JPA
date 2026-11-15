package uo.ri.ui.cashier.paymentmean.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeletePaymentMeanAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String id = Console.readString("Payment mean id");

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		service.deletePaymentMean(id);

		Console.println("The payment mean has been deleted");
	}

}
