package uo.ri.util.exception;

import java.util.List;
import java.util.Optional;

public class BusinessChecks {

	public static void isNull(Object o) throws BusinessException {
		isNull(o, o.getClass().getName() + " cannot be null here");
	}

	public static void isNull(Object o, String errorMsg)
			throws BusinessException {
		isTrue(o == null, errorMsg);
	}

	public static void isNotNull(Object o) throws BusinessException {
		isNotNull(o, o.getClass().getName() + " cannot be null here");
	}

	public static void isNotNull(Object o, String errorMsg)
			throws BusinessException {
		isTrue(o != null, errorMsg);
	}

	public static void isFalse(boolean condition) throws BusinessException {
		isFalse(condition, "Invalid assertion");
	}

	public static void isFalse(boolean condition, String errorMsg)
			throws BusinessException {
		isTrue( condition == false, errorMsg);
	}

	public static void exists(Optional<?> owo) throws BusinessException {
		exists(owo, "There is no such entity");
	}

	public static void exists(Optional<?> owo, String msg)
			throws BusinessException {
		isTrue(owo.isPresent(), msg);
	}

	public static void doesNotExist(Optional<?> owo) throws BusinessException {
		doesNotExist(owo, "The entity should not exist");
	}

	public static void doesNotExist(Optional<?> owo, String msg)
			throws BusinessException {
		isTrue(owo.isEmpty(), msg);
	}

	public static void isEmpty(List<?> l, String msg) throws BusinessException {
		isTrue(l.isEmpty(), msg);
	}

	public static void isNotEmpty(String str) throws BusinessException {
		isNotEmpty(str, "The string must not be empty");
	}

	public static void isNotEmpty(String str, String msg)
			throws BusinessException {
		isTrue(str != null && !str.isEmpty(), msg);
	}

	public static void isNotBlank(String str) throws BusinessException {
		isNotBlank(str, "Cannot be null nor blank");
	}

	public static void isNotBlank(String str, String msg) throws BusinessException {
		isTrue(str != null && !str.isBlank(), msg);
	}

	public static void hasVersion(long va, long vb) throws BusinessException {
		hasVersion(va, vb, "Versions do not match. You are working on stale data");
	}

	public static void hasVersion(long va, long vb, String msg) throws BusinessException {
		isTrue(va == vb, msg);
	}
	
	public static void isTrue(boolean condition) throws BusinessException {
		isTrue(condition, "Invalid assertion");
	}

	public static void isTrue(boolean condition, String msg) throws BusinessException {
		if (condition == false) {
			throw new BusinessException(msg);
		}
	}

}
