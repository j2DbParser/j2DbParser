package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULES_FILE;
import j2DbParser.db.IDatabase;
import j2DbParser.db.SqlDatabase;
import j2DbParser.db.viewer.IResultSetViewer;
import j2DbParser.db.viewer.ResultSetSimpleViewer;
import j2DbParser.io.DataReaderSystemIn;
import j2DbParser.io.IDataReader;
import j2DbParser.utils.IterableDecorator;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Parser insert data from file into database. It uses In-Memory database and
 * slow to execute selects from command line.
 */
public class ParserLive {
	private static final String EXE_NAME = "parserLive";

	public IResultSetViewer viewer = new ResultSetSimpleViewer();
	// new ResultSetTableViewer();

	public IDataReader reader = new DataReaderSystemIn();

	public static void main(String[] args) throws Exception {
		if (false) {
			args = EOptions.example(FILE, RULES_FILE);
			// args = EOptions.example(FILE, RULES_FILE, TREAT_AS);
		} else {
			// args = EOptions.example(RULES_FILE);
		}
		new ParserLive().start(args);
	}

	public void start(String[] args) throws Exception {
		boolean stop = new CommandLineSupport(EXE_NAME, args).parse();
		if (stop) {
			return;
		}

		// EOptions.show();
		Parser parser = getParser();
		IDatabase database = parser.database;
		database.setUseInMemory(true);
		SqlDatabase sqlDatabase = parser.insertIntoDatabase();
		Parser.showSelects(sqlDatabase);

		System.out.println("\nwrite selects here");

		for (String s : new IterableDecorator<String>(reader)) {
			System.out.println(s);
			if (s.toLowerCase().startsWith("select")) {
				ResultSet resultSet;
				try {
					resultSet = database.query(s);
					viewer.show(resultSet);
				} catch (SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		database.stop();
	}

	protected Parser getParser() throws IOException {
		return new Parser(FILE.value(), RULES_FILE.value());
	}

}
