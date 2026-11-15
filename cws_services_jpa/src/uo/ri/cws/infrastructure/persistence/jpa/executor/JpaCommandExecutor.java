package uo.ri.cws.infrastructure.persistence.jpa.executor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.cws.infrastructure.persistence.jpa.util.Jpa;
import uo.ri.util.exception.BusinessException;

public class JpaCommandExecutor implements CommandExecutor {

	@Override
	public <T> T execute(Command<T> cmd) throws BusinessException {
		EntityManager mapper = Jpa.createEntityManager();
		try {
			EntityTransaction trx = mapper.getTransaction();
			trx.begin();
			
			try {
				T res = cmd.execute();
				trx.commit();
				
				return res;
				
			} catch (Exception ex) {
				if ( trx.isActive() ) {
					trx.rollback();
				}
				throw ex;
			}
		} finally {
			if ( mapper.isOpen() ) {
				mapper.close();
			}
		}
	}
}
