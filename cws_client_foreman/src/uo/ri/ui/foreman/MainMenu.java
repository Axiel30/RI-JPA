package uo.ri.ui.foreman;

import uo.ri.ui.foreman.client.ClientsMenu;
import uo.ri.ui.foreman.reception.ReceptionMenu;
import uo.ri.ui.foreman.vehicle.VehiclesMenu;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class MainMenu extends BaseMenu {{
	menuOptions = new Object[][] { 
		{ "Foreman", null },
		{ "Vehicle reception", 		ReceptionMenu.class }, 
		{ "Client management", 		ClientsMenu.class },
		{ "Vehicle management", 	VehiclesMenu.class },
		{ "Review client history", 	NotYetImplementedAction.class }, 
	};
}}