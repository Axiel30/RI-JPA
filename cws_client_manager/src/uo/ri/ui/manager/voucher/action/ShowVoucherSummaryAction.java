package uo.ri.ui.manager.voucher.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ShowVoucherSummaryAction implements Action {

	@Override
	public void execute() throws BusinessException {
		VoucherGenerationService service = Factories.service.forVoucherService();
		List<VoucherSummaryDto> summary = service.getVoucherSummary();

		Console.println("\nVoucher summary\n");
		summary.forEach(row -> Printer.printVoucherSummary(row));
	}

}
