package uo.ri.ui.cashier.paymentmean.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindCreditCardByNumberAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String number = Console.readString("Credit card number");

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		Optional<CardDto> result = service.findCreditCardByNumber(number);
		if (!result.isPresent()) {
			throw new BusinessException("There is no credit card with that number");
		}

		Printer.printPaymentMean(result.get());
	}

}
