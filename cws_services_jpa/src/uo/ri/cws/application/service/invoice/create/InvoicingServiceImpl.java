package uo.ri.cws.application.service.invoice.create;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.commands.CreateInvoiceFor;
import uo.ri.cws.application.service.invoice.create.commands.FindNotInvoicedWorkOrdersByClientNif;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class InvoicingServiceImpl implements InvoicingService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public InvoiceDto create(List<String> woIds) throws BusinessException {
        return executor.execute(new CreateInvoiceFor(woIds));
    }

    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif) throws BusinessException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(String nif) throws BusinessException {
        return executor.execute(new FindNotInvoicedWorkOrdersByClientNif(nif));
    }

    @Override
    public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate) throws BusinessException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Optional<InvoiceDto> findInvoiceByNumber(Long number) throws BusinessException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<PaymentMeanDto> findPayMeansByClientNif(String nif) throws BusinessException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void settleInvoice(String invoiceId, Map<String, BigDecimal> charges) throws BusinessException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
