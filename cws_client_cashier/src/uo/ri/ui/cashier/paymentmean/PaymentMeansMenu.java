package uo.ri.ui.cashier.paymentmean;

import uo.ri.ui.cashier.paymentmean.action.AddCreditCardAction;
import uo.ri.ui.cashier.paymentmean.action.AddVoucherAction;
import uo.ri.ui.cashier.paymentmean.action.DeletePaymentMeanAction;
import uo.ri.ui.cashier.paymentmean.action.FindCreditCardByNumberAction;
import uo.ri.ui.cashier.paymentmean.action.FindPaymentMeanByIdAction;
import uo.ri.ui.cashier.paymentmean.action.FindPaymentMeansByClientNifAction;
import uo.ri.ui.cashier.paymentmean.action.FindVoucherByCodeAction;
import uo.ri.util.menu.BaseMenu;

public class PaymentMeansMenu extends BaseMenu {

	public PaymentMeansMenu() {
		menuOptions = new Object[][] {
			{ "Cashier > Payment mean management", null },

			{ "Add credit card", 				AddCreditCardAction.class },
			{ "Add voucher", 					AddVoucherAction.class },
			{ "Delete payment mean", 			DeletePaymentMeanAction.class },
			{ "Find payment mean by id", 		FindPaymentMeanByIdAction.class },
			{ "Find payment means by client nif", FindPaymentMeansByClientNifAction.class },
			{ "Find credit card by number", 	FindCreditCardByNumberAction.class },
			{ "Find voucher by code", 			FindVoucherByCodeAction.class },
		};
	}

}
