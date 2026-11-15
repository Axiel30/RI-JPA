package uo.ri.cws.application.service.acceptance.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import uo.ri.util.exception.BusinessException;

public class ExceptionBox {
	@FunctionalInterface
	public interface Action {
		void execute() throws Exception;
	}

	private Exception exception;
	
	public void tryAndKeep(Action action) {
		try {
			action.execute();
			fail("An exception was expected");
		} catch (Exception e) {
			this.exception = e;
		}
	}

	public void assertBusinessExceptionWithMessage() {
		assertNotNull( exception );
		assertTrue(exception instanceof BusinessException);
		assertNotNull( exception.getMessage() );
		assertFalse( exception.getMessage().isBlank() );	
	}

	public void assertIllegalArgumentExceptionWithMessage() {
		assertNotNull(exception);
		assertTrue(exception instanceof IllegalArgumentException);
		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
