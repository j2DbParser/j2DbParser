package j2DbParser.dbunit;

import org.dbunit.DBTestCase;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

public class DbUnitTest extends DBTestCase {

	static {
		DbIniter.init();
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return getConnection().createDataSet();
	}

	public void testDb() throws Exception {
		System.out.println("testDb()");
		IDatabaseConnection connection = getConnection();
		System.out.println("connection=" + connection);

	}
}
