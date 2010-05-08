package j2DbParser.cli;

import static org.junit.Assert.assertEquals;
import j2DbParser.AbstractTestParser;
import j2DbParser.io.RulesReader;

import java.util.Map;

import org.junit.Test;

public class ParserStartTest extends AbstractTestParser {

	@Test
	public void singleLine() throws Throwable {
		SqlInsertObserverAssert asserts = new SqlInsertObserverAssert() {
			@Override
			protected void check(Map<String, String> map) {
				String key;

				key = "time";
				assertEquals("2010-03-19 14:30:01,341", map.get(key));
				map.remove(key);
				key = "level";
				assertEquals("INFO", map.get(key));
				map.remove(key);
				key = "data";
				assertEquals("Quartz scheduler version: 1.5.2", map.get(key));
				map.remove(key);
				key = "class";
				assertEquals("org.quartz.impl.StdSchedulerFactory", map
						.get(key));
				map.remove(key);
				assertEquals(0, map.size());
			}

		};
		String source = "2010-03-19 14:30:01,341 INFO  [org.quartz.impl.StdSchedulerFactory] Quartz scheduler version: 1.5.2";
		RulesReader rules = getExampleRules();
		testLog(rules, source, asserts);
	}

}
