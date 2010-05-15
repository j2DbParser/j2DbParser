package j2DbParser.hooks;

import j2DbParser.db.SqlColumn;

import java.util.Set;

public interface IPostCreateTable extends IHook {
	void runCreate(String table, Set<SqlColumn> columns, String createQuery);
}
