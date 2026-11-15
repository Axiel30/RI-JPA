package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.InvoiceRepository;
import uo.ri.cws.application.repository.WorkOrderRepository;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.create.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.WorkOrder;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class CreateInvoiceFor implements Command<InvoiceDto> {

    private List<String> workOrderIds;
    private WorkOrderRepository woRepo = Factories.repository.forWorkOrder();
    private InvoiceRepository invRepo = Factories.repository.forInvoice();

    public CreateInvoiceFor(List<String> workOrderIds) {
        ArgumentChecks.isNotNull(workOrderIds, "Work order ids cannot be null");
        ArgumentChecks.isFalse(workOrderIds.isEmpty(), "Work order ids cannot be empty");
        ArgumentChecks.isFalse(workOrderIds.stream().anyMatch(i -> i == null), "Work order id cannot be null");

        this.workOrderIds = workOrderIds;
    }

    @Override
    public InvoiceDto execute() throws BusinessException {
        List<WorkOrder> workOrders = woRepo.findByIds(workOrderIds);

        for (String id : workOrderIds) {
            boolean found = workOrders.stream().anyMatch(w -> w.getId().equals(id));
            if (!found) {
                throw new BusinessException("Work order not found: " + id);
            }
        }

        for (WorkOrder wo : workOrders) {
            if (!wo.isApproved()) {
                throw new BusinessException("Work order is not in APPROVED state: " + wo.getId());
            }
        }

        Long number = Factories.query.forInvoice().getNextInvoiceNumber();
        Invoice invoice = new Invoice(number, workOrders);
        invRepo.add(invoice);

        return DtoAssembler.toDto(invoice);
    }

}
