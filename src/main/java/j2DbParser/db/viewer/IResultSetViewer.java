package j2DbParser.db.viewer;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class presents result of sql query.
 */
public interface IResultSetViewer {
	/**
	 * Present {@link ResultSet}
	 * 
	 * @param resultSet
	 *            resultSet
	 * @throws SQLException
	 *             execption
	 */
	void show(ResultSet resultSet) throws SQLException;
}