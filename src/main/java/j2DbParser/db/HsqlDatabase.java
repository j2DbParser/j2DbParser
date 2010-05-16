package j2DbParser.db;

import java.sql.DriverManager;

import org.hsqldb.Server;

/**
 * Default database, allows to use in memory database(visible only in current
 * jdbc session).
 */
public class HsqlDatabase extends IniDatabase {

	private Server hsqlServer;

	private boolean useInMemory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() throws Exception {
		Class.forName("org.hsqldb.jdbcDriver");
		url = "jdbc:hsqldb:hsql://localhost/xdb";
		if (useInMemory) {
			// tables are only visible in the same jdbc session
			url = "jdbc:hsqldb:mem:aname";
		}
		con = DriverManager.getConnection(url, "SA", "");
	}

	@Override
	public void start() {
		hsqlServer = new Server();
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);
		hsqlServer.setDatabaseName(0, "xdb");
		hsqlServer.setDatabasePath(0, "file:testdb");
		hsqlServer.start();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		if (hsqlServer != null) {
			hsqlServer.stop();
		}
	}

	@Override
	public void setUseInMemory(boolean useInMemory) {
		this.useInMemory = useInMemory;
	}
}
