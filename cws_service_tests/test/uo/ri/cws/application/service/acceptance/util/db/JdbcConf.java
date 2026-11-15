package uo.ri.cws.application.service.acceptance.util.db;

import java.io.IOException;
import java.util.Properties;

public class JdbcConf {
	private static Properties props;
	private static final String FILE_CONF = "connection.properties";

	static {
		props = new Properties();
		try {
			props.load(JdbcConf.class.getClassLoader().getResourceAsStream(FILE_CONF));
		} catch (IOException e) {
			throw new RuntimeException("File properties cannot be loaded",e);
		}
		
	}
	
	public static String getProperty(String key) {
		String value = props.getProperty(key);
		if (value == null) {
			throw new RuntimeException("Property not found in config file");
		}
		return value;
	}
	

}
