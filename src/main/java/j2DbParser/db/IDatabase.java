package j2DbParser.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Database actions
 */
public interface IDatabase {

	void start() throws Exception;

	void closeConnection() throws Exception;

	void stop() throws Exception;

	void insert(String table, Map<String, String> map) throws Exception;

	int getAdded();

	void createTables(SqlDatabase db) throws Exception;

	ResultSet query(String s) throws SQLException;

	void setUseInMemory(boolean useInMemory);

	String getUrl();

}