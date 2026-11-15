package uo.ri.cws.application.service.acceptance.util.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtil {

//	public static Connection createConnection() throws SQLException {
//		return SimpleDataSource.getConnection();
//	}

	public static Connection createConnection() throws SQLException {
		return C3p0DataSource.getConnection();
	}
}
