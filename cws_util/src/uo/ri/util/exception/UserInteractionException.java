package uo.ri.util.exception;

@SuppressWarnings("serial")
public class UserInteractionException extends Exception {

	public UserInteractionException() {
	}

	public UserInteractionException(String message) {
		super(message);
	}

	public UserInteractionException(Throwable cause) {
		super(cause);
	}

	public UserInteractionException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserInteractionException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
