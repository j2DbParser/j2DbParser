package j2DbParser.io;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Reads data from {@link System#in}
 */
public class DataReaderSystemIn extends DataReader {

	public DataReaderSystemIn() {
		super(null);
	}

	@Override
	public Scanner initScanner() throws FileNotFoundException {
		return new Scanner(System.in);
	}

}
