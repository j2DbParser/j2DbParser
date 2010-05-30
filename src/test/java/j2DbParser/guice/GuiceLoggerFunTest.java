package j2DbParser.guice;


import java.util.logging.Logger;

import org.junit.Test;

import com.google.inject.Inject;

public class GuiceLoggerFunTest {

	public static class LoggerFun {
		@Inject
		Logger log;

		public void logAll() {
			log.severe("severe");
			log.warning("warning");
			log.info("info");
			log.config("config");

			log.fine("fine");
			log.finer("finer");
			log.finest("finest");

			log.entering("entering", "param");
			log.exiting("exiting", "param");

			log.throwing("throwing", "throwingParam",
					new IllegalStateException("bla"));
		}
	}

	@Test
	public void testLogging() throws Exception {
		LoggerFun logger = Guicer.getInstance(LoggerFun.class);
		logger.logAll();
	}
}
