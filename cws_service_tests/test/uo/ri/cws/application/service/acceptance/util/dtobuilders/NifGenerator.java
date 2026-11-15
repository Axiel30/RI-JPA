package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import uo.ri.util.random.Random;

public class NifGenerator {

    private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    public static String generate() {

        int number = 10000000 + Random.nextInt(90000000); // 8 digits

        char letter = calculateLetter(number);

        return number + String.valueOf(letter);
    }

    private static char calculateLetter(int number) {
        return LETTERS.charAt(number % 23);
    }
}