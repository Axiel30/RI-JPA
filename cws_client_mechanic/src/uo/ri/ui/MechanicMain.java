package uo.ri.ui;

import uo.ri.conf.Factories;
import uo.ri.ui.mechanic.MainMenu;
import uo.ri.util.console.DefaultPrinter;

public class MechanicMain {

	private MainMenu menu = new MainMenu();
	
	public static void main(String[] args) {
		new MechanicMain()
			.config()
			.run()
			.close();
	}

	private MechanicMain config() {
		return this;
	}

	public MechanicMain run() {
		try {
			menu.execute();

		} catch (RuntimeException rte) {
			DefaultPrinter.printRuntimeError(rte);
		}
		return this;
	}

	private void close() {
		Factories.close();
	}

}
