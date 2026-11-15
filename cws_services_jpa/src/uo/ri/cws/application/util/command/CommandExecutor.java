package uo.ri.cws.application.util.command;

import uo.ri.util.exception.BusinessException;

public interface CommandExecutor {

	<T> T execute(Command<T> cmd) throws BusinessException;

}