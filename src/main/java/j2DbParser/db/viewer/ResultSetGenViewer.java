package j2DbParser.db.viewer;

import j2DbParser.utils.StringsUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ResultSetGenViewer implements IResultSetViewer {
	/**
	 * {@inheritDoc}
	 */
	public void show(ResultSet resultSet) throws SQLException {
		final ResultSetMetaData meta = resultSet.getMetaData();
		final int columnCount = meta.getColumnCount();
		System.out.println("while (rs.next()) {");
		for (int i = 1; i < columnCount + 1; i++) {
			String columnName = meta.getColumnName(i);
			columnName = columnName.toLowerCase();
			columnName = StringsUtils.wordJoiner(columnName, "_");
			String type = meta.getColumnClassName(i);
			type = type.replace("java.lang.", "");
			System.out.println("\t" + type + " " + columnName + " = (" + type
					+ ") rs.getObject(" + i + ");");
		}
		System.out.println("}");
	}
}
