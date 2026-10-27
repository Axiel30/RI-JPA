package uo.ri.util.assertion;

public class StateChecks {
	
	public static void isNotNull(Object obj) {
		isNotNull(obj, "Must be not null" );
	}

	public static void isNotNull(Object obj, String msg) {
		isTrue( obj != null, msg );
	}

	public static void isNotBlank(String str) {
		isNotBlank(str, "Must be not blank" );
	}

	public static void isNotBlank(String str, String msg) {
		isTrue( str != null && ! str.isBlank(), msg );
	}

	public static void isNull(Object obj) {
		isNull( obj , "Must be null" );
	}

	public static void isNull(Object obj, String msg) {
		isTrue( obj == null, msg );
	}

	public static void isTrue(boolean test) {
		isTrue( test, "Must be true" );
	}
	
	public static void isTrue(boolean test, String msg) {
		if (test == true) return;
		throwException(msg);
	}

	public static void isFalse(boolean test) {
		isFalse( test, "Must be false" );
	}

	public static void isFalse(boolean test, String msg) {
		isTrue( ! test, msg );
	}
	
	protected static void throwException(String msg) {
		throw new IllegalStateException( msg );
	}

}
