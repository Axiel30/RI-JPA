package uo.ri.ui.cashier.paymentmean.action;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddCreditCardAction implements Action {

	private static final DateTimeFormatter EXPIRATION_FORMAT =
			DateTimeFormatter.ofPattern("MM/uuuu");

	@Override
	public void execute() throws BusinessException {
		ClientDto client = PaymentMeanActionSupport.findClientByNif();

		CardDto card = new CardDto();
		card.clientId = client.id;
		card.cardNumber = Console.readString("Credit card number");
		card.cardType = Console.readString("Credit card type");
		card.cardExpiration = readExpiration();

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		card = service.addCard(card);

		Console.println("New credit card added");
		Printer.printPaymentMean(card);
	}

	private YearMonth readExpiration() {
		String expiration = Console.readString("Expiration date (MM/yyyy)");
		return YearMonth.parse(expiration, EXPIRATION_FORMAT);
	}

}
