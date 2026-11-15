package uo.ri.cws.application.service.acceptance.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uo.ri.cws.application.service.acceptance.util.db.ConnectionDataLoader.ConnectionData;

public class SimpleDataSource {
	private static ConnectionData conData = ConnectionDataLoader.getConnectionData();

	public static Connection getConnection() throws SQLException {
			return DriverManager.getConnection(
					conData.url,
					conData.user, 
					conData.pass
			);

	}

}
