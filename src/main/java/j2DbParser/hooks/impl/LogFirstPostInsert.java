package j2DbParser.hooks.impl;

import j2DbParser.hooks.IPostInsert;
import j2DbParser.system.LogFactory;

import java.util.Map;
import java.util.logging.Logger;

public class LogFirstPostInsert implements IPostInsert {

	private static final Logger log = LogFactory
			.getLogger(LogFirstPostInsert.class);

	private boolean notFirst;

	public boolean runInsert(String table, Map<String, String> map) {
		if (notFirst) {
			return false;
		}
		log.warning("runInsert(" + table + ", " + map + ")");
		notFirst = true;
		return false;
	}

}
