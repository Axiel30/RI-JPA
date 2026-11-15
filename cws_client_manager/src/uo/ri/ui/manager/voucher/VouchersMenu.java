package uo.ri.ui.manager.voucher;

import uo.ri.ui.manager.voucher.action.FindVouchersByClientNifAction;
import uo.ri.ui.manager.voucher.action.GenerateVouchersAction;
import uo.ri.ui.manager.voucher.action.ShowVoucherSummaryAction;
import uo.ri.util.menu.BaseMenu;

public class VouchersMenu extends BaseMenu {

	public VouchersMenu() {
		menuOptions = new Object[][] {
			{ "Manager > Voucher management", null },

			{ "Generate vouchers", 			GenerateVouchersAction.class },
			{ "List vouchers by client nif", 	FindVouchersByClientNifAction.class },
			{ "Show voucher summary", 		ShowVoucherSummaryAction.class },
		};
	}

}
