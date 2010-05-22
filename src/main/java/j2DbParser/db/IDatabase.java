package j2DbParser.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.google.inject.ImplementedBy;

/**
 * Database actions
 */
@ImplementedBy(IniDatabase.class)
public interface IDatabase {
	void open() throws Exception;

	void closeConnection() throws Exception;

	void stop() throws Exception;

	boolean insert(String table, Map<String, String> map) throws Exception;

	int getAdded();

	void createTables(SqlDatabase db) throws Exception;

	ResultSet query(String s) throws SQLException;

	void setUseInMemory(boolean useInMemory);

	String getUrl();

	void start();
}