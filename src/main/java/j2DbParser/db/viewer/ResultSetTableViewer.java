package j2DbParser.db.viewer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

public class ResultSetTableViewer implements IResultSetViewer {
	/**
	 * {@inheritDoc}
	 */
	public void show(ResultSet resultSet) throws SQLException {
		final ResultSetMetaData meta = resultSet.getMetaData();
		final int columnCount = meta.getColumnCount();

		String size = "70"; // TODO: count it?

		String[] asName = new String[columnCount];
		for (int i = 1; i < columnCount + 1; i++) {
			String columnName = meta.getColumnName(i);
			columnName = StringUtils.rightPad(columnName, 15, ' ');
			asName[i - 1] = columnName;
		}

		// TODO: use one format?
		System.out.format(genFormat(size, columnCount), (Object[]) asName);

		while (resultSet.next()) {
			String[] as = new String[columnCount];
			for (int i = 1; i < columnCount + 1; i++) {
				Object object = resultSet.getObject(i);
				String ns = object != null ? object.toString() : "null";
				// ns = StringUtils.rightPad(ns, 15, ' ');
				as[i - 1] = ns;
			}
			System.out.format(genFormat(size, columnCount), (Object[]) as);
		}
	}

	private String genFormat(String size, int columnCount) {
		// TODO: generate format
		return "%1$-" + size + "s|%2$-" + size + "s|%3$-" + size + "s|"
				+ "%4$-" + size + "s" + "\n";
	}
}
