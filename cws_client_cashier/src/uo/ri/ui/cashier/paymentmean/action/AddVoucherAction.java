package uo.ri.ui.cashier.paymentmean.action;

import java.math.BigDecimal;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.client.ClientCrudService.ClientDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddVoucherAction implements Action {

	@Override
	public void execute() throws BusinessException {
		ClientDto client = PaymentMeanActionSupport.findClientByNif();

		VoucherDto voucher = new VoucherDto();
		voucher.clientId = client.id;
		voucher.code = Console.readString("Voucher code");
		voucher.description = Console.readString("Description");
		voucher.availableAmount = Console.readBigDecimal("Available amount");
		voucher.accumulatedAmount = BigDecimal.ZERO;

		PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();
		voucher = service.addVoucher(voucher);

		Console.println("New voucher added");
		Printer.printPaymentMean(voucher);
	}

}
