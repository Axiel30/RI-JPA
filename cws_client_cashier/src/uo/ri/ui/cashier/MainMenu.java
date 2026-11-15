package uo.ri.ui.cashier;

import uo.ri.ui.cashier.action.FindNotInvoicedWorkOrdersByClientAction;
import uo.ri.ui.cashier.action.FindWorkOrdersByClientAction;
import uo.ri.ui.cashier.action.FindWorkOrdersByPlateAction;
import uo.ri.ui.cashier.action.InvoiceWorkorderAction;
import uo.ri.ui.cashier.action.SettleInvoiceAction;
import uo.ri.ui.cashier.paymentmean.PaymentMeansMenu;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {{
	menuOptions = new Object[][] {
		{ "Cashier", null },
		
		{ "Find not invoiced work orders by client", 	FindNotInvoicedWorkOrdersByClientAction.class },
		{ "Find work orders by client", 				FindWorkOrdersByClientAction.class },
		{ "Find work orders by plate", 					FindWorkOrdersByPlateAction.class },
		{ "Invoice work orders", 						InvoiceWorkorderAction.class },
		{ "Settle invoice", 							SettleInvoiceAction.class },
		{ "Payment mean management", 					PaymentMeansMenu.class },
	};
}}
