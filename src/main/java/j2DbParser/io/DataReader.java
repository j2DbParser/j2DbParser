package j2DbParser.io;

import j2DbParser.system.StopperSingleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.lang.NotImplementedException;

/**
 * Default implementation of {@link IDataReader}
 */
public class DataReader implements IDataReader {
	private final String logFile;
	private Scanner scanner;

	public DataReader(String logFile) {
		this.logFile = logFile;
		if (logFile != null && !new File(logFile).canRead()) {
			StopperSingleton.getInstance().stop(
					"Could not read the file " + logFile);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		if (scanner == null) {
			try {
				scanner = initScanner();
			} catch (FileNotFoundException e) {
				StopperSingleton.getInstance().stop(e);
				return false;
			}
		}
		return scanner.hasNextLine();
	}

	/**
	 * {@inheritDoc}
	 */
	public Scanner initScanner() throws FileNotFoundException {
		return new Scanner(new FileInputStream(logFile).getChannel());
	}

	/**
	 * {@inheritDoc}
	 */
	public String next() {
		return scanner.nextLine();
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove() {
		throw new NotImplementedException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException {
		if (scanner != null) {
			scanner.close();
		}
	}

}
