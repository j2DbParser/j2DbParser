package j2DbParser;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import j2DbParser.cli.Parser;
import j2DbParser.cli.ParserStartTest;
import j2DbParser.db.IDatabase;
import j2DbParser.db.SqlDatabase;
import j2DbParser.io.DataReader;
import j2DbParser.io.RulesReader;
import j2DbParser.junit.PartialMockFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.easymock.IExpectationSetters;

public class AbstractTestParser {

	protected static class SqlInsertAnswer extends Observable implements
			IAnswer<Object> {
		public Object answer() throws Throwable {
			Object[] args = EasyMock.getCurrentArguments();
			String table = (String) args[0];
			assertEquals("log", table);

			Map<String, String> columnDataMap = (Map<String, String>) args[1];
			setChanged();
			notifyObservers(columnDataMap);
			return null;
		}
	}

	protected static abstract class SqlInsertObserverAssert implements Observer {
		public void update(Observable o, Object arg) {
			Map<String, String> columnDataMap = (Map<String, String>) arg;
			check(columnDataMap);
		}

		abstract protected void check(Map<String, String> columnDataMap);
	}

	protected static class TestGenSqlInsertObserverAssert implements Observer {
		public void update(Observable o, Object arg) {
			Map<String, String> map = (Map<String, String>) arg;

			Set<Entry<String, String>> entrySet = map.entrySet();
			List list = new ArrayList(entrySet);
			for (Entry<String, String> mp : entrySet) {
				if (mp.equals(list.get(0))) {
					System.out
							.println("Iterator < Entry<String, String> > it = map.entrySet().iterator();\nEntry<String, String> next;\n");
				}
				System.out.println("next = it.next();");

				System.out.println("assertEquals(\"" + mp.getKey()
						+ "\", next." + "getKey" + "());");
				System.out.println("assertEquals(\"" + mp.getValue()
						+ "\", next." + "getValue" + "());");
				if (mp.equals(list.get(list.size() - 1))) {
					System.out.println("assertFalse(it.hasNext());");
				}
			}
		}
	}

	protected void testStart(RulesReader rules, String source,
			SqlInsertObserverAssert asserts) throws Throwable {
		try {
			IDatabase database = createStrictMock(IDatabase.class);
			database.start();
			database.createTables((SqlDatabase) anyObject());

			database.insert((String) anyObject(),
					(Map<String, String>) anyObject());
			IExpectationSetters<Object> expectLastCall = EasyMock
					.expectLastCall();
			SqlInsertAnswer insertAnswer = new SqlInsertAnswer();
			expectLastCall.andAnswer(insertAnswer);

			expect(database.getAdded()).andAnswer(new IAnswer<Integer>() {

				int counter;

				public Integer answer() throws Throwable {
					return ++counter;
				}
			});

			database.closeConnection();

			expect(database.getUrl()).andReturn("url");

			replay(database);

			DataReader reader = getDataReaderMock(source);

			Parser parser = new Parser(null, null);
			parser.setConfig(new Config());
			parser.setDatabase(database);
			parser.setRules(rules);
			parser.setReader(reader);

			// insertAnswer.addObserver(new TestGenSqlInsertObserverAssert());
			insertAnswer.addObserver(asserts);
			parser.start();
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
	}

	protected DataReader getDataReaderMock(String source)
			throws FileNotFoundException {
		DataReader reader = PartialMockFactory.createMock(DataReader.class,
				"initScanner");
		Scanner scanner = new Scanner(source);
		expect(reader.initScanner()).andReturn(scanner).anyTimes();
		replay(reader);
		while (false && reader.hasNext()) {
			System.out.println("reader.next()=" + reader.next());
		}
		return reader;
	}

	protected RulesReader getExampleRules() throws IOException {
		RulesReader rules = new RulesReader(ParserStartTest.class.getResource(
				"example-rules.properties").getFile());
		return rules;
	}

}