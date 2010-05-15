package j2DbParser.hooks.impl;

import j2DbParser.ConfigSingleton;
import j2DbParser.hooks.IPostInsert;
import j2DbParser.system.LogFactory;

import java.util.Map;
import java.util.logging.Logger;

public class MaxInsertPostInsert implements IPostInsert {

	private static final int MAX = ConfigSingleton.getInstance().maxInsertCount;
	public static final Logger log = LogFactory
			.getLogger(MaxInsertPostInsert.class);
	private int count = 1;

	public boolean runInsert(String table, Map<String, String> map) {
		count++;
		// log.warning("runInsert(" + count + ")");
		if (count == MAX) {
			count = 0;
			return true;
		}
		return false;
	}

}
