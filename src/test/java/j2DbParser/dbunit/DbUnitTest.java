package j2DbParser.dbunit;

import org.dbunit.DBTestCase;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

public class DbUnitTest extends DBTestCase {

	static {
		new DbIniter();
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
