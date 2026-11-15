package uo.ri.cws.application.service.acceptance.util.db;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction {

	public interface Command {
		void execute(Connection con) throws SQLException;
	}

	public void execute(Command cmd) {
		try {
			Connection con = DataSourceUtil.createConnection();
			try {

				cmd.execute(con);
				con.commit();

			} catch (Exception e) {
				con.rollback();
				throw e;
			} finally {
				con.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
