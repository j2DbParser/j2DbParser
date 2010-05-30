package j2DbParser;

import java.io.File;

import org.ini4j.Wini;

import com.google.inject.Singleton;

/**
 * Configuration
 */
@Singleton
public class ConfigSingleton {
	private static final int MAX_COLUMN_LENGTH_DEFAULT = 50000;
	static final int MAX_INSERT_COUNT_SAMPLE = 50;
	static final int MAX_INSERT_COUNT_NONE = -1;

	// TODO: transform into console option
	public int maxColumnLength = MAX_COLUMN_LENGTH_DEFAULT;
	public int maxInsertCount = MAX_INSERT_COUNT_NONE;
	public boolean autoDropTables = false;

	public Wini read() throws Exception {
		return new Wini(new File("config.ini"));
	}

}
