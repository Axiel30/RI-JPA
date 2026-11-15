package uo.ri.ui.manager.voucher.action;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindVouchersByClientNifAction implements Action {

	@Override
	public void execute() throws BusinessException {
		String nif = Console.readString("Client nif");

		VoucherGenerationService service = Factories.service.forVoucherService();
		List<VoucherDto> vouchers = service.findVouchersByClientNif(nif);

		Console.println("\nClient vouchers\n");
		vouchers.forEach(v -> Printer.printVoucher(v));
		Printer.printVoucherTotals(
				vouchers.size(),
				totalAmount(vouchers),
				consumedAmount(vouchers),
				availableAmount(vouchers)
			);
	}

	private BigDecimal totalAmount(List<VoucherDto> vouchers) {
		BigDecimal consumed = consumedAmount(vouchers);
		BigDecimal available = availableAmount(vouchers);
		return consumed.add(available);
	}

	private BigDecimal consumedAmount(List<VoucherDto> vouchers) {
		return vouchers.stream()
				.map(v -> v.accumulatedAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal availableAmount(List<VoucherDto> vouchers) {
		return vouchers.stream()
				.map(v -> v.availableAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
