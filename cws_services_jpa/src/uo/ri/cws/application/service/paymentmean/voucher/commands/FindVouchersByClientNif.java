package uo.ri.cws.application.service.paymentmean.voucher.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.voucher.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVouchersByClientNif implements Command<List<VoucherDto>> {

    private Nif nif;
    private PaymentMeanRepository repo = Factories.repository.forPaymentMean();

    public FindVouchersByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "Nif cannot be null or blank");
        this.nif = new Nif(nif);
    }

    @Override
    public List<VoucherDto> execute() throws BusinessException {
        List<Voucher> vouchers = repo.findVouchersByClientNif(nif);
        return DtoAssembler.toDtoList(vouchers);
    }

}
