package j2DbParser.db;

import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

public class DbUnitTest extends DBTestCase {

	static {
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				"com.mysql.jdbc.Driver");
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				"jdbc:mysql://localhost/so-temp");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				"qazxsw");
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return getConnection().createDataSet();
	}

	public void testname() throws Exception {
		System.out.println("testname()");
		IDatabaseConnection connection = getConnection();
		System.out.println("connection=" + connection);

	}
}
