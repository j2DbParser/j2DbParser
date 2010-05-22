package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.db.IDatabase;
import j2DbParser.db.SqlColumn;
import j2DbParser.db.SqlDatabase;
import j2DbParser.hooks.HookRunner;
import j2DbParser.io.DataReader;
import j2DbParser.io.RulesReader;
import j2DbParser.system.LogFactory;
import j2DbParser.utils.IterableDecorator;
import j2DbParser.xpath.XPathStaXParser;
import j2DbParser.xpath.XmlObserver;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.apache.commons.lang.NotImplementedException;

import com.google.common.collect.ImmutableBiMap;
import com.google.inject.Guice;
import com.google.inject.Inject;

/**
 * Parser insert data from file into database.
 */
public class Parser {
	private static final String EXEC_NAME = "parser";

	private static final Logger log = LogFactory.getLogger(Parser.class);

	@Inject
	public IDatabase database;
	// new HsqlDatabase(configSingleton);
	// new IniDatabase();

	public RulesReader rules;
	public DataReader reader;
	private String filename;

	public static void main(String[] args) throws Exception {
		new CommandLineSupport(EXEC_NAME, args).parse();

		String rulesFile = RULE_NAME.value();
		String logFile = FILE.value();

		Parser parser = Guice.createInjector().getInstance(Parser.class);
		parser.start(logFile, rulesFile);
	}

	public void start(String filename, String ruleName) throws Exception {
		this.filename = filename;
		rules = new RulesReader(ruleName);
		reader = new DataReader(filename);

		IDatabase user = Guice.createInjector().getInstance(IDatabase.class);
		System.out.println("user=" + user);

		SqlDatabase db = insertIntoDatabase();

		database.closeConnection();
		// System.out.println("done");
		showSelects(db);
		// database.stop();

		Thread.sleep(100);
		System.out.println("\nYou can now connect to database - "
				+ database.getUrl());
	}

	public SqlDatabase insertIntoDatabase() throws Exception {
		database.start();
		database.open();

		SqlDatabase db = new SqlDatabase(rules);
		database.createTables(db);
		HookRunner.POST_CREATE_TABLES.run();

		process(db, rules);
		return db;
	}

	private void process(SqlDatabase db, RulesReader rules) throws Exception {
		EFileType type = EOptions.type;
		log.info("type1=" + type);
		switch (type) {
		case LOG:
			readLog(db, rules);
			break;
		case XML:
			readXml(db, rules);
			break;
		default:
			throw new NotImplementedException("unsupported file type");
		}
		HookRunner.POST_INSERTS.run();
	}

	private void readXml(SqlDatabase db, RulesReader rules) throws Exception {
		final Map<String, String> rulesMap = rules.getRulesMap();
		Collection<String> xpathes = rulesMap.values();
		String[] xpaths = xpathes.toArray(new String[xpathes.size()]);
		System.out.println("xpathes=" + xpathes);
		System.out.println("filename=" + filename);

		final ImmutableBiMap<String, String> rulesMapBi = ImmutableBiMap
				.copyOf(rulesMap).inverse();

		// final Map<String, Set<SqlColumn>> dbMap = db.getMap();

		XPathStaXParser xparser = new XPathStaXParser();
		xparser.addObserver(new XmlObserver() {
			@Override
			public boolean updateLine(Map<String, String> map) throws Exception {
				// System.out.println("updateLine(" + map + ")");

				Map<String, String> columnDataMap = new LinkedHashMap<String, String>();

				String table = null;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					// System.out.println(entry);
					String xpath = entry.getKey();
					String value = entry.getValue();

					String identString = rulesMapBi.get(xpath);
					table = SqlDatabase.extractTableName(identString);
					String column = SqlColumn.extractColumnName(identString);

					columnDataMap.put(column, value);
				}
				boolean stop = database.insert(table, columnDataMap);
				return stop;
			}
		});
		xparser.parse(filename, xpaths);
	}

	private void readLog(SqlDatabase db, RulesReader rules) throws Exception {
		try {
			int added = 0;
			for (String string : IterableDecorator.create(reader)) {
				// System.out.println("string=" + string);
				if (insertData(rules, db, string)) {
					break;
				}
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

	/**
	 * 
	 * 
	 * @param rules
	 * @param db
	 * @param string
	 * @return stop processing
	 * @throws Exception
	 */
	private boolean insertData(RulesReader rules, SqlDatabase db, String string)
			throws Exception {
		Map<String, String> rulesMap = rules.getRulesMap();
		Map<String, Set<SqlColumn>> tableMap = db.getMap();

		Map<String, Map<String, String>> dataMap = lineAsMap(rulesMap,
				tableMap, string);

		for (Map.Entry<String, Map<String, String>> entry : dataMap.entrySet()) {
			String table = entry.getKey();
			Map<String, String> columnDataMap = entry.getValue();
			if (!columnDataMap.isEmpty()) {
				boolean stop = database.insert(table, columnDataMap);
				return stop;
			}
		}
		return false;
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
