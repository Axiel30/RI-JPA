package uo.ri.ui.manager.spares;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class SparePartsManagementMenu extends BaseMenu {

	public SparePartsManagementMenu() {
		menuOptions = new Object[][] {
			{"Manager > Parts management", null},
			
			{ "Add", 		NotYetImplementedAction.class }, 
			{ "Update", 	NotYetImplementedAction.class }, 
			{ "Remove", 	NotYetImplementedAction.class }, 
			{ "List all", 	NotYetImplementedAction.class },
		};
	}

}
