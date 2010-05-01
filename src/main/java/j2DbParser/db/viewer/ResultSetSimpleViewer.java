package j2DbParser.db.viewer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

public class ResultSetSimpleViewer implements IResultSetViewer {
	private static final int DEFAULT_COLUMN_SIZE = 70;

	/**
	 * {@inheritDoc}
	 */
	public void show(ResultSet resultSet) throws SQLException {
		final ResultSetMetaData meta = resultSet.getMetaData();
		final int columnCount = meta.getColumnCount();

		StringBuffer b = new StringBuffer();
		for (int i = 1; i < columnCount + 1; i++) {
			String columnName = meta.getColumnName(i);
			columnName = StringUtils.rightPad(columnName, DEFAULT_COLUMN_SIZE, ' ');
			b.append(columnName);
		}
		System.out.println(b.toString());

		while (resultSet.next()) {
			final StringBuilder sb = new StringBuilder();
			for (int i = 1; i < columnCount + 1; i++) {
				Object object = resultSet.getObject(i);
				String ns = object != null ? object.toString() : "null";
				ns = StringUtils.rightPad(ns, 70, ' ');
				sb.append(ns);
			}
			System.out.println(sb.toString());
		}
	}
}
