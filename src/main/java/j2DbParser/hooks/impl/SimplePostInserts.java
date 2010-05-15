package j2DbParser.hooks.impl;

import j2DbParser.hooks.IPostInserts;
import j2DbParser.system.LogFactory;

import java.util.logging.Logger;

public class SimplePostInserts implements IPostInserts {

	private static final Logger log = LogFactory
			.getLogger(SimplePostInserts.class);

	public void run() {
		log.warning("run(SimplePostInserts)");
	}

}
