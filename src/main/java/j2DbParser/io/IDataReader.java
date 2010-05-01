package j2DbParser.io;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Reads data using {@link Scanner} and supports testing.
 */
public interface IDataReader extends Iterator<String>, Closeable {
	// for test
	Scanner initScanner() throws FileNotFoundException;
}