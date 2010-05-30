package j2DbParser.db;

import j2DbParser.system.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SqlColumn {

	private static final Logger log = LogFactory.getLogger(SqlColumn.class);
	public String name;
	public Pattern pattern;
	public String type;

	public SqlColumn(String name) {
		this.name = name;
	}

	public SqlColumn() {
	}

	public SqlColumn init(String key) {
		name = extractColumnName(key);
		if (key.indexOf("(") != -1) {
			type = StringUtils.substringAfter(key, "(");
			type = StringUtils.substringBeforeLast(type, ")");
		}
		return this;
	}

	public static String extractColumnName(String key) {
		String s = StringUtils.substringAfter(key, ".");
		if (s.indexOf("(") != -1) {
			// column type
			s = StringUtils.substringBefore(s, "(");
		}
		return s;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.NO_FIELD_NAMES_STYLE);
	}

	public static String asCommaSeperated(Collection<SqlColumn> col) {
		List<SqlColumn> al = null;
		if (col instanceof ArrayList) {
			al = (ArrayList<SqlColumn>) col;
		} else {
			al = new ArrayList<SqlColumn>(col);
		}
		StringBuilder sb = new StringBuilder();
		for (Iterator<SqlColumn> iterator = al.iterator(); iterator.hasNext();) {
			String next = iterator.next().name;
			sb.append(next);
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return (sb.toString());
	}

	public String getColumnType(int maxColumnLength) {
		String out;
		out = "TEXT";
		if (type != null) {
			out = type;
		} else {
			// out = out + "(" + maxColumnLength + ")";
		}
		log.warning(name + "=" + out);
		return out;
	}

}
