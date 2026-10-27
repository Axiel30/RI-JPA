package uo.ri.util.random;

import java.math.BigDecimal;
import java.util.List;

public class Random {

	private Random() {
	}

	public static int nextInt(int bound) {
		return (int) inRange(0, bound);
	}

	public static int inRange(int min, int max) {
		return (int) inRange((double) min, (double) max);
	}

	public static long inRange(long min, long max) {
		return (long) inRange((double) min, (double) max);
	}
	
	public static double nextDouble() {
		return inRange(0.0, 1.0);
	}

	public static double nextDouble(double bound) {
		return inRange(0.0, bound);
	}

	public static double inRange(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static BigDecimal randomPrice(int minCents, int maxCents) {
	    int randomCents = inRange(minCents, maxCents + 1);
	    return BigDecimal.valueOf(randomCents, 2); // scale = 2 → cents
	}

	public static String string(int length) {
		String res = "";
		for (int i = 0; i < length; i++) {
			res += (char) inRange('A', 'Z');
		}
		return res;
	}

	public static String string(char min, char max, int length) {
		String res = "";
		for (int i = 0; i < length; i++) {
			res += (char) inRange(min, max + 1);
		}
		return res;
	}

	public static int choose(int... options) {
		return options[inRange(0, options.length)];
	}

	public static boolean bool() {
		return inRange(0, 2) == 0;
	}

	public static boolean chance(int percentage) {
		return inRange(0, 100) < percentage;
	}

	@SafeVarargs
	public static <T> T oneOf(T... options) {
		return options[inRange(0, options.length)];
	}

	public static <T> T oneOf(List<T> list) {
		return list.get(inRange(0, list.size()));
	}


}