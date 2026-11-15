package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.cws.domain.types.Nif;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClientNif implements Command<List<InvoicingWorkOrderDto>> {

    private Nif nif;
    private WorkOrderRepository repo = Factories.repository.forWorkOrder();

    public FindNotInvoicedWorkOrdersByClientNif(String nif) {
        ArgumentChecks.isNotBlank(nif, "Client NIF cannot be null or blank");
        this.nif = new Nif(nif);
    }

    @Override
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
        List<WorkOrder> workOrders = repo.findNotInvoicedByClientNif(nif);
        return DtoAssembler.toInvoicingWorkOrderDtoList(workOrders);
    }

}
