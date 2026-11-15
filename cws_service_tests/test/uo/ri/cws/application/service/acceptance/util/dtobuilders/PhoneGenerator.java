package uo.ri.cws.application.service.acceptance.util.dtobuilders;

import java.util.concurrent.ThreadLocalRandom;

public final class PhoneGenerator {

	public static String generate() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        
	    // Spain (+34) followed by a 9-digit subscriber number.
	    long subscriber = rnd.nextLong(600_000_000L, 800_000_000L);

	    return "+34" + subscriber;
	}
}
