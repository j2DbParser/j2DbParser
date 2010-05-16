package j2DbParser.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogFactory {
	private static final String LOGGING_PROPERTIES = "logging.properties";

	static {
		loadFromProperties();
	}

	private static void loadFromProperties() {
		try {
			InputStream stream = null;
			try {
				stream = ClassLoader
						.getSystemResourceAsStream(LOGGING_PROPERTIES);
				LogManager.getLogManager().readConfiguration(stream);
			} finally {
				if (stream != null) {
					stream.close();
				}
			}
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Logger getLogger(Class<?> clazz) {
		// System.out.println("getLogger(" + clazz + ")");
		return Logger.getLogger(clazz.getName());
	}

}
