package uo.ri.util.console;

import java.io.PrintStream;

/**
 * Utility methods for controlled screen output.
 * Here you would add all relevant decorations.
 * 
 * @author alb
 */
public class DefaultPrinter {
	private static PrintStream con = System.out;

	public static void printHeading(String string) {
		con.println(string);
	}

	/**
	 * Warns of a logical error during execution, most likely due to
	 * user mistake or circumstances that have changed during the user's
	 * "think time" (optimistic control, etc.)
	 * 
	 * @param e
	 */
	public static void printBusinessError(Exception e) {
		con.println("\033[1;30m"); 
		con.println("A problem occurred while processing your option:");
		con.println("\t- " + e.getLocalizedMessage());
		con.println("\033[0m"); 
	}

	/**
	 * Warns of an unrecoverable error
	 * @param e
	 */
	public static void printRuntimeError(RuntimeException e) {
		con.println("An unrecoverable internal error has occurred, " 
				+ "the program must terminate.\n");

		// Print message in bold black using ANSI escape codes
		con.println(
			"\033[1;30m" 
				+ "[A stack trace of the error is shown below]\n" 
				+ "[The stack trace would not be visible to the user when the "
					+ "application is in production]\n" 
				+ "[It would be sent to the system log]\n" 
			+ "\033[0m"
		);
		
		e.printStackTrace();
	}

	public static void print(String msg) {
		con.println(msg);
	}

	public static void printException(String msg, Exception e) {
		con.println(msg);
		con.println("\t- " + e.getLocalizedMessage());
	}

}