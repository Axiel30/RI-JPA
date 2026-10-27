package uo.ri.util.exception;

import java.util.Optional;

public class UserInteractionChecks {

	public static void isNull(Object o, String errorMsg)
			throws UserInteractionException {
		isTrue(o == null, errorMsg);
	}

	public static void isNull(Object o) throws UserInteractionException {
		isTrue(o == null, o.getClass().getName() + " cannot be null here");
	}

	public static void isNotNull(Object o, String errorMsg)
			throws UserInteractionException {
		isTrue(o != null, errorMsg);
	}

	public static void isNotNull(Object o) throws UserInteractionException {
		isTrue(o != null, o.getClass().getName() + " cannot be null here");
	}

	public static void isFalse(boolean condition) throws UserInteractionException {
		isTrue(!condition, "Invalid assertion");
	}

	public static void isFalse(boolean condition, String errorMsg)
			throws UserInteractionException {
		isTrue(!condition, errorMsg);
	}

	public static void exists(Optional<?> owo, String msg)
			throws UserInteractionException {
		isTrue(owo.isPresent(), msg);
	}

	public static void exists(Optional<?> owo) throws UserInteractionException {
		exists(owo, "There is no such entity");
	}

	public static void isNotEmpty(String str, String msg)
			throws UserInteractionException {
		isTrue(str != null && !str.isEmpty(), msg);
	}

	public static void isTrue(boolean condition) throws UserInteractionException {
		isTrue(condition, "Invalid assertion");
	}

	public static void isTrue(boolean condition, String msg)
			throws UserInteractionException {
		if (condition == false) {
			throw new UserInteractionException(msg);
		}
	}

}
