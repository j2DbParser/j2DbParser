package j2DbParser.hooks.impl;

import j2DbParser.db.SqlColumn;
import j2DbParser.hooks.IPostCreateTable;
import j2DbParser.system.logging.LogFactory;

import java.util.Set;
import java.util.logging.Logger;

public class LogPostCreateTable implements IPostCreateTable {

	private static final Logger log = LogFactory
			.getLogger(LogPostCreateTable.class);

	public void runCreate(String table, Set<SqlColumn> columns,
			String createQuery) {
		log.warning(createQuery);
		log.warning("columns=" + columns);
		log.warning("runCreate(" + table + ")");
	}

}
