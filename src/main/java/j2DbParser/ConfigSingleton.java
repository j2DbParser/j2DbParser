package j2DbParser;

import j2DbParser.system.LogFactory;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ini4j.Wini;

/**
 * Configuration
 */
public class ConfigSingleton {
	private static final int MAX_COLUMN_LENGTH_DEFAULT = 50000;
	static final int MAX_INSERT_COUNT_SAMPLE = 50;
	static final int MAX_INSERT_COUNT_NONE = -1;

	// TODO: transform into console option
	public int maxColumnLength = MAX_COLUMN_LENGTH_DEFAULT;
	public int maxInsertCount = MAX_INSERT_COUNT_SAMPLE;
	public boolean autoDropTables = true;

	private static final Logger log = LogFactory
			.getLogger(ConfigSingleton.class);

	public Wini read() throws Exception {
		return new Wini(new File("config.ini"));
	}

	/**
	 * The unique instance of this class.
	 */
	private static ConfigSingleton instance = new ConfigSingleton();

	/**
	 * Prevent instances of this class from being created.
	 */
	private ConfigSingleton() {
		log.warning("this = "
				+ ToStringBuilder.reflectionToString(this,
						ToStringStyle.MULTI_LINE_STYLE));
	}

	/**
	 * Return the unique instance of this class.
	 * 
	 * @return the unique instance of this class
	 */
	public static ConfigSingleton getInstance() {
		return instance;
	}

	/**
	 * For mocking
	 * 
	 * @param instance
	 *            mock instance
	 */
	public static void setInstance(ConfigSingleton instance) {
		System.out.println("ConfigSingleton.setInstance(" + instance + ")");
		ConfigSingleton.instance = instance;
	}
}
