package uo.ri.ui;

import uo.ri.conf.Factories;
import uo.ri.ui.foreman.MainMenu;
import uo.ri.util.console.DefaultPrinter;

public class ForemanMain {

	public static void main(String[] args) {
		new ForemanMain()
			.config()
			.run()
			.close();
	}

	private ForemanMain config() {
		return this;
	}

	public ForemanMain run() {
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
