package j2DbParser.db;

import j2DbParser.Config;
import j2DbParser.cli.Parser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.hsqldb.Server;

/**
 * Default database, allows to use in memory database(visible only in current
 * jdbc session).
 */
public class HsqlDatabase implements IDatabase {

	private static final Logger log = Logger.getLogger(Parser.class.getName());

	private final Config config;
	private Connection con;
	private Server hsqlServer;
	private int added;

	private boolean useInMemory;

	private String url;

	public HsqlDatabase(Config config) {
		this.config = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public void start() throws Exception {
		hsqlServer = new Server();
		hsqlServer.setLogWriter(null);
		hsqlServer.setSilent(true);
		hsqlServer.setDatabaseName(0, "xdb");
		hsqlServer.setDatabasePath(0, "file:testdb");
		hsqlServer.start();

		Class.forName("org.hsqldb.jdbcDriver");
		url = "jdbc:hsqldb:hsql://localhost/xdb";
		if (useInMemory) {
			// tables are only visible in the same jdbc session
			url = "jdbc:hsqldb:mem:aname";
		}
		con = DriverManager.getConnection(url, "SA", "");
	}

	/**
	 * {@inheritDoc}
	 */

	public void closeConnection() throws Exception {
		if (con != null) {
			con.close();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws Exception
	 */

	public void stop() throws Exception {
		closeConnection();
		if (hsqlServer != null) {
			hsqlServer.stop();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void createTables(SqlDatabase db) throws Exception {
		Map<String, Set<SqlColumn>> tables = db.getMap();
		int maxColumnLength = config.maxColumnLength;
		for (Map.Entry<String, Set<SqlColumn>> entry : tables.entrySet()) {
			String table = entry.getKey();
			Set<SqlColumn> columns = entry.getValue();
			String q = "";
			for (Iterator<SqlColumn> iterator = columns.iterator(); iterator
					.hasNext();) {
				SqlColumn column = iterator.next();
				String type = "VARCHAR";
				if (false) {
					if (column.type != null) {
						type = column.type;
					}
				}
				q += column.name + " " + type + "(" + maxColumnLength + ")";

				if (iterator.hasNext()) {
					q += ", ";
				}
			}
			String s = "create table " + table + " (" + q + ");";
			log.info(s);
			try {
				con.prepareStatement(s).execute();
			} catch (java.sql.SQLException e) {
				System.err.println("e=" + e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */

	public void insertSimple(String table, String column, String data)
			throws Exception {
		String s = "insert into " + table + "(" + column + ") values (?);";
		log.info(s);
		PreparedStatement st = con.prepareStatement(s);
		st.setString(1, data);
		st.execute();
	}

	/**
	 * {@inheritDoc}
	 */

	public void insert(String table, Map<String, String> map) throws Exception {
		int maxColumnLength = config.maxColumnLength;
		String s = genInsert(table, map);
		PreparedStatement st;
		try {
			st = con.prepareStatement(s);
		} catch (SQLException e) {
			System.err.println("insert=" + s);
			e.printStackTrace();
			return;
		}
		int i = 1;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			// System.out.println(entry);
			// String key = entry.getKey();
			String value = entry.getValue();
			int length = value.length();
			if (length > maxColumnLength) {
				value = value.substring(0, maxColumnLength);
				System.err.println("value.length=" + length);
			}
			st.setString(i++, value); // TODO: named params?
		}
		st.execute();
		added++;
	}

	protected String genInsert(String table, Map<String, String> map) {
		Set<String> keySet = map.keySet();

		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (@SuppressWarnings("unused")
		String string : keySet) {
			sb.append(delim).append("?");
			delim = ", ";
		}
		String questionMarks = sb.toString();

		String columnNames = StringUtils.join(keySet, ", ");

		String s = "insert into " + table + "(" + columnNames + ") values ("
				+ questionMarks + ");";
		// System.out.println(s);
		return s;
	}

	public int getAdded() {
		return added;
	}

	public ResultSet query(String s) throws SQLException {
		Statement st = null;
		try {
			st = con.createStatement();
			return st.executeQuery(s);
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}

	public void setUseInMemory(boolean useInMemory) {
		this.useInMemory = useInMemory;
	}

	public String getUrl() {
		return url;
	}

}
