package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.db.IDatabase;
import j2DbParser.db.SqlDatabase;
import j2DbParser.db.viewer.IResultSetViewer;
import j2DbParser.db.viewer.ResultSetSimpleViewer;
import j2DbParser.io.DataReaderSystemIn;
import j2DbParser.io.IDataReader;
import j2DbParser.system.LogFactory;
import j2DbParser.utils.IterableDecorator;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Parser insert data from file into database. It uses In-Memory database and
 * slow to execute selects from command line.
 */
public class ParserLive {

	private static final Logger log = LogFactory.getLogger(ParserLive.class);
	private static final String EXE_NAME = "parserLive";

	public IResultSetViewer viewer = new ResultSetSimpleViewer();
	// new ResultSetTableViewer();

	public IDataReader reader = new DataReaderSystemIn();

	public static void main(String[] args) throws Exception {
		new ParserLive().start(args);
	}

	public void start(String[] args) throws Exception {
		new CommandLineSupport(EXE_NAME, args).parse();

		// EOptions.show();
		Parser parser = getParser();
		IDatabase database = parser.database;
		database.setUseInMemory(true);
		SqlDatabase sqlDatabase = parser.insertIntoDatabase();
		Parser.showSelects(sqlDatabase);

		System.out.println("\nwrite selects here");

		for (String s : IterableDecorator.create(reader)) {
			System.out.println(s);
			if (s.toLowerCase().startsWith("select")) {
				ResultSet resultSet;
				try {
					resultSet = database.query(s);
					viewer.show(resultSet);
				} catch (SQLException e) {
					String message = e.getMessage();
					System.err.println(message);
					log.warning(message);
					log.throwing("ParserLive", "database.query", e);
				}
			}
		}
		database.stop();
	}

	protected Parser getParser() throws IOException {
		return new Parser(FILE.value(), RULE_NAME.value());
	}

}
