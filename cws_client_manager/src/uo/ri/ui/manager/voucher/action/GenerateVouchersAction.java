package uo.ri.ui.manager.voucher.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GenerateVouchersAction implements Action {

	@Override
	public void execute() throws BusinessException {
		VoucherGenerationService service = Factories.service.forVoucherService();

		int[] generated = service.generateVouchers();
		int generatedForWorkOrders = generated[0];
		int generatedForRecommendations = generated[1];
		int generatedForInvoices = generated[2];

		Console.println();
		Console.println("Voucher generation finished");
		Console.printf("\t20 euro vouchers for work orders: %d\n", generatedForWorkOrders);
		Console.printf("\t25 euro vouchers for recommendations: %d\n", generatedForRecommendations);
		Console.printf("\t30 euro vouchers for invoices: %d\n", generatedForInvoices);
	}

}
