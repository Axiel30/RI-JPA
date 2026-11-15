package uo.ri.cws.application.service.acceptance.util.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import uo.ri.cws.application.service.acceptance.util.db.ConnectionDataLoader.ConnectionData;

public class C3p0DataSource {
	private static ConnectionData conData = ConnectionDataLoader.getConnectionData();
    private static ComboPooledDataSource pool = new ComboPooledDataSource();

    static {
        try {
            pool.setDriverClass( conData.driver );
            pool.setJdbcUrl( conData.url );
            pool.setUser( conData.user );
            pool.setPassword( conData.pass );

        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

}
