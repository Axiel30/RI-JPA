package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.PaymentMeanDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindPaymentMeanById implements Command<Optional<PaymentMeanDto>> {

    private String id;
    private PaymentMeanRepository repo = Factories.repository.forPaymentMean();

    public FindPaymentMeanById(String id) {
        ArgumentChecks.isNotBlank(id, "Id cannot be null or blank");
        this.id = id;
    }

    @Override
    public Optional<PaymentMeanDto> execute() throws BusinessException {
        return repo.findById(id).map(DtoAssembler::toDto);
    }

}
