package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.query.PaymentMeanQueries;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class GetVoucherSummary implements Command<List<VoucherSummaryDto>> {

    private PaymentMeanQueries query = Factories.query.forPaymentMean();

    @Override
    public List<VoucherSummaryDto> execute() throws BusinessException {
        return query.getVouchersSummary();
    }

}
