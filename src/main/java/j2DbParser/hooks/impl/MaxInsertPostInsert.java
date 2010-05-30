package j2DbParser.hooks.impl;

import j2DbParser.ConfigSingleton;
import j2DbParser.hooks.IPostInsert;
import j2DbParser.system.logging.LogFactory;

import java.util.Map;
import java.util.logging.Logger;

import com.google.inject.Inject;

public class MaxInsertPostInsert implements IPostInsert {

	@Inject
	public ConfigSingleton config;

	public static final Logger log = LogFactory
			.getLogger(MaxInsertPostInsert.class);
	private int count = 1;

	public boolean runInsert(String table, Map<String, String> map) {
		int MAX = config.maxInsertCount;
		count++;
		// log.warning("runInsert(" + count + ")");
		if (count == MAX) {
			count = 0;
			return true;
		}
		return false;
	}

}
