package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindVoucherByCode implements Command<Optional<VoucherDto>> {

    private VoucherCode code;
    private PaymentMeanRepository repo = Factories.repository.forPaymentMean();

    public FindVoucherByCode(String code) {
        ArgumentChecks.isNotBlank(code, "Code cannot be null or blank");
        this.code = new VoucherCode(code);
    }

    @Override
    public Optional<VoucherDto> execute() throws BusinessException {
        return repo.findVoucherByCode(code).map(DtoAssembler::toDto);
    }

}
