package uo.ri.ui;

import uo.ri.conf.Factories;
import uo.ri.ui.manager.MainMenu;
import uo.ri.util.console.DefaultPrinter;

public class ManagerMain {

	public static void main(String[] args) {
		new ManagerMain()
			.configure()
			.run()
			.close();
	}

	private ManagerMain configure() {
		return this;
	}

	private ManagerMain run() {
		try {
			new MainMenu().execute();

		} catch (RuntimeException rte) {
			DefaultPrinter.printRuntimeError(rte);
		}
		return this;
	}

	private void close() {
		Factories.close();
	}

}
