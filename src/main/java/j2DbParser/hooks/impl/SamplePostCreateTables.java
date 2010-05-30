package j2DbParser.hooks.impl;

import j2DbParser.hooks.IPostCreateTables;
import j2DbParser.system.logging.LogFactory;

import java.util.logging.Logger;

public class SamplePostCreateTables implements IPostCreateTables {

	private static final Logger log = LogFactory
			.getLogger(SamplePostCreateTables.class);

	public void run() {
		log.warning("run(SamplePostCreateTables)");
	}

}
