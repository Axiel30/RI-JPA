package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindPayMeansByClientNif implements Command<List<PaymentMeanDto>> {

    private String nif;
    private PaymentMeanRepository pmRepo = Factories.repository.forPaymentMean();

    public FindPayMeansByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "NIF cannot be null or blank");
        new Nif(nif);
        this.nif = nif;
    }

    @Override
    public List<PaymentMeanDto> execute() throws BusinessException {
        List<PaymentMean> pms = pmRepo.findPaymentMeansByClientNif(new Nif(nif));
        return DtoAssembler.toPaymentMeanDtoList(pms);
    }
}