package uo.ri.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindPaymentMeanByIdAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String id = Console.readString("Payment mean id");

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		Optional<PaymentMeanDto> result = service.findById(id);
		if (!result.isPresent()) {
			throw new BusinessException("There is no payment mean with that id");
		}

		Printer.printPaymentMean(result.get());
	}

}
