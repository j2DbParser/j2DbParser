package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULES_FILE;
import j2DbParser.Config;
import j2DbParser.db.HsqlDatabase;
import j2DbParser.db.IDatabase;
import j2DbParser.db.SqlColumn;
import j2DbParser.db.SqlDatabase;
import j2DbParser.io.DataReader;
import j2DbParser.io.RulesReader;
import j2DbParser.utils.IterableDecorator;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * Parser insert data from file into database.
 */
public class Parser {
	private static final String EXEC_NAME = "parser";

	private static final Logger log = Logger.getLogger(Parser.class.getName());
	// TODO: configure the damn logger

	public Config config = new Config();
	public IDatabase database = new HsqlDatabase(config);
	public RulesReader rules;
	public DataReader reader;

	public Parser(String logFile, String rulesFile) throws IOException {
		rules = new RulesReader(rulesFile);
		reader = new DataReader(logFile);
	}

	public static void main(String[] args) throws Exception {
		if (false) {
			args = EOptions.example(FILE, RULES_FILE);
			// args = EOptions.example(FILE, RULES_FILE, TREAT_AS);
		} else {
			// args = EOptions.example(RULES_FILE);
		}

		boolean stop = new CommandLineSupport(EXEC_NAME, args).parse();
		if (stop) {
			return;
		}
		String rulesFile = RULES_FILE.value();
		String logFile = FILE.value();
		new Parser(logFile, rulesFile).start();
	}

	public void start() throws Exception {
		SqlDatabase db = insertIntoDatabase();

		database.closeConnection();
		// System.out.println("done");
		showSelects(db);
		// database.stop();

		System.out.println("\nYou can now connect to database - "
				+ database.getUrl());
	}

	public SqlDatabase insertIntoDatabase() throws Exception {
		database.start();

		SqlDatabase db = new SqlDatabase(rules);
		database.createTables(db);

		process(db, rules);
		return db;
	}

	private void process(SqlDatabase db, RulesReader rules) throws Exception {
		try {
			int added = 0;
			for (String string : new IterableDecorator<String>(reader)) {
				// System.out.println("string=" + string);
				insertData(rules, db, string);
				added = database.getAdded();
				if (added % 500 == 0) {
					log.info("added=" + added);
				}
			}
			log.info("added=" + added);
		} finally {
			reader.close();
		}
	}

	public static void showSelects(SqlDatabase db) {
		for (Map.Entry<String, Set<SqlColumn>> entry : db.getMap().entrySet()) {
			String table = entry.getKey();
			Set<SqlColumn> list = entry.getValue();
			String columns = SqlColumn.asCommaSeperated(list);
			log.info("SELECT count(*) FROM " + table);
			log.info("SELECT " + columns + " FROM " + table);
		}
	}

	private void insertData(RulesReader rules, SqlDatabase db, String string)
			throws Exception {
		Map<String, String> rulesMap = rules.getRulesMap();
		Map<String, Set<SqlColumn>> tableMap = db.getMap();

		Map<String, Map<String, String>> dataMap = lineAsMap(rulesMap,
				tableMap, string);

		for (Map.Entry<String, Map<String, String>> entry : dataMap.entrySet()) {
			String table = entry.getKey();
			Map<String, String> columnDataMap = entry.getValue();
			if (!columnDataMap.isEmpty()) {
				database.insert(table, columnDataMap);
			}
		}
	}

	private Map<String, Map<String, String>> lineAsMap(
			Map<String, String> rulesMap, Map<String, Set<SqlColumn>> tableMap,
			String string) {

		Map<String, Map<String, String>> dataMap = new LinkedHashMap<String, Map<String, String>>(
				tableMap.size());
		// <table,<column.name,column.data>>

		for (Map.Entry<String, Set<SqlColumn>> entry : tableMap.entrySet()) {
			String table = entry.getKey();
			Set<SqlColumn> columnList = entry.getValue();
			int size = columnList.size();
			for (SqlColumn column : columnList) {
				Map<String, String> mapList = dataMap.get(table);
				if (mapList == null) {
					mapList = new LinkedHashMap<String, String>(size);
					dataMap.put(table, mapList);
				}

				Matcher matcher = column.pattern.matcher(string);
				if (matcher.find() && matcher.groupCount() > 0) {
					String data = matcher.group(1).trim();
					mapList.put(column.name, data);
				}
			}
		}
		return dataMap;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setDatabase(IDatabase database) {
		this.database = database;
	}

	public void setRules(RulesReader rules) {
		this.rules = rules;
	}

	public void setReader(DataReader reader) {
		this.reader = reader;
	}

}
