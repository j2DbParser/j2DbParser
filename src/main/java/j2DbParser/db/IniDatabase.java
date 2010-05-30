package j2DbParser.db;

import j2DbParser.ConfigSingleton;
import j2DbParser.hooks.HookRunner;
import j2DbParser.system.StopperSingleton;
import j2DbParser.system.logging.LogFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ini4j.Wini;

import com.google.inject.Inject;

public class IniDatabase implements IDatabase {

	protected static final Logger log = LogFactory.getLogger(IniDatabase.class);

	protected Connection con;
	protected String url;

	int added;

	@Inject
	public ConfigSingleton config;

	@Override
	public void open() throws Exception {
		Wini ini = config.read();
		String db = ini.get("config", "default_db");
		log.finest("db=" + db);
		url = ini.get(db, "url");
		String user = ini.get(db, "user");
		String password = ini.get(db, "password");
		String driver = ini.get(db, "driver");
		Class.forName(driver);
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			StopperSingleton.getInstance().stop();
		}
	}

	@Override
	public void setUseInMemory(boolean useInMemory) {
		// hook
	}

	@Override
	public void start() {
		// hook
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createTables(SqlDatabase db) throws Exception {
		Map<String, Set<SqlColumn>> tables = db.getMap();
		int maxColumnLength = config.maxColumnLength;
		for (Map.Entry<String, Set<SqlColumn>> entry : tables.entrySet()) {
			String table = entry.getKey();
			Set<SqlColumn> columns = entry.getValue();
			String createInsides = "";
			for (Iterator<SqlColumn> iterator = columns.iterator(); iterator
					.hasNext();) {
				SqlColumn column = iterator.next();
				String type = column.getColumnType(maxColumnLength);

				createInsides += column.name + " " + type;

				if (iterator.hasNext()) {
					createInsides += ", ";
				}
			}
			String createQuery = "create table " + table + " (" + createInsides
					+ ");";
			log.info(createQuery);
			try {
				con.prepareStatement(createQuery).execute();
			} catch (SQLException e) {
				int code = e.getErrorCode();
				String message = e.getMessage();
				switch (code) {
				case 1074:
					// Column length too big for column 'LastAccessDate' (max =
					// 65535); use BLOB or TEXT instead
					System.err.println(message);
					log.warning(message);
					StopperSingleton.getInstance().stop();
					break;
				case 1050:
					if (config.autoDropTables) {
						try {
							System.err.println("dropping table " + table
									+ "...");
							con.prepareStatement("drop table " + table)
									.executeUpdate();
							con.prepareStatement(createQuery).execute();
						} catch (SQLException e1) {
							message = e1.getMessage();
							System.err.println(message);
							log.warning(message);
						}
					}
					break;
				default:
					System.out.println("code=" + code);

					System.err.println(message);
					log.warning(message);
					break;
				}
			}
			HookRunner.POST_CREATE_TABLE.runCreate(table, columns, createQuery);
		}
	}

	@Override
	public ResultSet query(String query) throws SQLException {
		Statement st = null;
		try {
			st = con.createStatement();
			return st.executeQuery(query);
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void insertSimple(String table, String column, String data)
			throws Exception {
		String query = "insert into " + table + "(" + column + ") values (?);";
		log.info(query);
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(query);
			st.setString(1, data);
			st.execute();
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}

	Map<String, PreparedStatement> statementCache = new HashMap<String, PreparedStatement>();

	/**
	 * {@inheritDoc}
	 * 
	 * @return stop processing
	 */
	@Override
	public boolean insert(String table, Map<String, String> map)
			throws Exception {
		int maxColumnLength = config.maxColumnLength;
		String query = genInsert(table, map);

		PreparedStatement st = statementCache.get(query);
		if (st == null) {
			try {
				st = con.prepareStatement(query);
				statementCache.put(query, st);
			} catch (SQLException e) {
				System.err.println("insert=" + query);
				e.printStackTrace();
				return false;
			}
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
		try {
			st.execute();
		} catch (Exception e) {
			System.err.println(e.getMessage() + "\r\n" + "for" + "\r\n" + st);
		}
		added++;
		return HookRunner.POST_INSERT.runInsert(table, map);
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

		String query = "insert into " + table + "\r\n(" + columnNames
				+ ") values \r\n(" + questionMarks + ");";
		// System.out.println(query);
		return query;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	public void stop() throws Exception {
		closeConnection();
	}

	@Override
	public int getAdded() {
		return added;
	}

	@Override
	public String getUrl() {
		return url;
	}

}