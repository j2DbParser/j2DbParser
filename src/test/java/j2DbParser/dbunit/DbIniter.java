package j2DbParser.dbunit;

import java.io.IOException;
import java.util.Properties;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;

public enum DbIniter {
	driver(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS), url(
			PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL), username(
			PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME), pwd(
			PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD);

	private final String value;

	private DbIniter(String value) {
		this.value = value;
	}

	private static final String PROP = "dbunit.properties";

	public static void init() {
		try {
			Properties prop = new Properties();
			prop.load(ClassLoader.getSystemResourceAsStream(PROP));
			DbIniter[] values = values();
			for (DbIniter dbIniter : values) {
				String property = prop.getProperty(dbIniter.name());
				System.setProperty(dbIniter.value, property);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
