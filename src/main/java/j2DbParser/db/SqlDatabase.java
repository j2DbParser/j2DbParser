package j2DbParser.db;

import j2DbParser.io.RulesReader;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class SqlDatabase {

	private static final Logger log = Logger.getLogger(SqlDatabase.class
			.getName());

	private final Map<String, Set<SqlColumn>> map;

	public SqlDatabase(RulesReader rules) {
		map = asTables(rules.getRulesMap());
	}

	private Map<String, Set<SqlColumn>> asTables(Map<String, String> keySet) {
		Map<String, Set<SqlColumn>> map = new HashMap<String, Set<SqlColumn>>();

		for (Map.Entry<String, String> entry : keySet.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			String table = extractTableName(key);
			Set<SqlColumn> mapList = map.get(table);
			if (mapList == null) {
				mapList = new LinkedHashSet<SqlColumn>();
				map.put(table, mapList);
			}
			SqlColumn column = new SqlColumn().init(key);
			log.fine(column.toString());
			column.pattern = Pattern.compile(value);
			mapList.add(column);
		}
		return map;
	}

	private String extractTableName(String key) {
		String table = StringUtils.substringBefore(key, ".");
		return table;
	}

	public Map<String, Set<SqlColumn>> getMap() {
		return map;
	}

}
