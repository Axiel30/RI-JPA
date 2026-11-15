package uo.ri.ui;

import uo.ri.conf.Factories;
import uo.ri.ui.cashier.MainMenu;
import uo.ri.util.console.DefaultPrinter;

public class CashierMain {

	public static void main(String[] args) {
		new CashierMain()
				.config()
				.run()
				.close();
	}

	private CashierMain config() {
		return this;
	}

	public CashierMain run() {
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
