package uo.ri.ui.manager;

import uo.ri.ui.manager.mechanic.MechanicsMenu;
import uo.ri.ui.manager.spares.SparePartsManagementMenu;
import uo.ri.ui.manager.vehicletype.VehicleTypesMenu;
import uo.ri.ui.manager.voucher.VouchersMenu;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {{
		menuOptions = new Object[][] {
			{ "Manager", null },

			{ "Mechanics management", 		MechanicsMenu.class },
			{ "Spare parts management", 	SparePartsManagementMenu.class },
			{ "Vehicle types management", 	VehicleTypesMenu.class },
			{ "Voucher management", 		VouchersMenu.class },
		};
}}
