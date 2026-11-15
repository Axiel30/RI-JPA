package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;

public interface ServiceFactory {

	// Foreman use cases
	VehicleCrudService forVehicleCrudService();
	ClientCrudService forClientCrudService();
	ClientHistoryService forClientHistoryService();
	WorkOrderCrudService forWorkOrderCrudService();

	// Mechanic use cases
	CloseWorkOrderService forClosingWorkOrder();
	ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService();

	// Manager use cases
    MechanicCrudService forMechanicCrudService();
    VehicleTypeCrudService forVehicleTypeCrudService();
    SparePartCrudService forSparePartCrudService();
    VoucherGenerationService forVoucherService();

    // Cashier use cases
    InvoicingService forCreateInvoiceService();
    PaymentMeanCrudService forPaymentMeanCrudService();

}
