package j2DbParser.hooks;

import java.util.Map;

public interface IPostInsert extends IHook {
	/**
	 * 
	 * 
	 * @param table
	 * @param map
	 * @return stop processing
	 */
	boolean runInsert(String table, Map<String, String> map);
}
