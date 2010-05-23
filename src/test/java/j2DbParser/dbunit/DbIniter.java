package j2DbParser.dbunit;

import java.io.IOException;
import java.util.Properties;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;

public class DbIniter {
	private static final String PROP = "dbunit.properties";

	static {
		try {
			Properties prop = new Properties();
			prop.load(ClassLoader.getSystemResourceAsStream(PROP));
			System.out.println("prop=" + prop);
			System.setProperty(
					PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, prop
							.getProperty("driver"));
			System.setProperty(
					PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
					prop.getProperty("url"));
			System.setProperty(
					PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, prop
							.getProperty("username"));
			System.setProperty(
					PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, prop
							.getProperty("pwd"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public static void main(String[] args) throws Exception {
		new DbIniter();
	}
}
