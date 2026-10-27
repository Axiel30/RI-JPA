package uo.ri.util;

import uo.ri.util.random.Random;

/**
 * Utility class to generate random names, surnames, dnis and strings for 
 * testing purposes.
 */
public class Values {

    private static String newPrefixedString(String prefix) {
        return prefix + "-" + Random.string(5);
    }

	/**
	 * Generates a new random string prefixed with "name-".
	 */
    public static String newName() {
        return newPrefixedString("name");
    }

    /**
     * Generates a new random string prefixed with "surname-".
     */
    public static String newSurname() {
        return newPrefixedString("surname");
    }

	/**
	 * Generates a new random string prefixed with "nif-".
	 */
    public static String newNif() {
        return "nif-" + Random.inRange(1, 1000000);
    }

	/**
	 * Generates a new string with a given prefix and a random suffix of given
	 * length.
	 * 
	 * @param prefix the prefix for the string.
	 * @param len    the length of the random suffix.
	 */
    public static String newString(String prefix, int len) {
        return prefix + "-" + Random.string(len);
    }

}
