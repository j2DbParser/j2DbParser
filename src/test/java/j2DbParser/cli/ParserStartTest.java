package j2DbParser.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import j2DbParser.AbstractTestParser;
import j2DbParser.io.RulesReader;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class ParserStartTest extends AbstractTestParser {

	@Test
	public void singleLine() throws Throwable {
		SqlInsertObserverAssert asserts = new SqlInsertObserverAssert() {
			@Override
			protected void check(Map<String, String> map) {
				Iterator<Entry<String, String>> it = map.entrySet().iterator();
				Entry<String, String> next;

				next = it.next();
				assertEquals("class", next.getKey());
				assertEquals("org.quartz.impl.StdSchedulerFactory", next
						.getValue());
				next = it.next();
				assertEquals("level", next.getKey());
				assertEquals("INFO", next.getValue());
				next = it.next();
				assertEquals("time", next.getKey());
				assertEquals("2010-03-19 14:30:01,341", next.getValue());
				next = it.next();
				assertEquals("data", next.getKey());
				assertEquals("Quartz scheduler version: 1.5.2", next.getValue());
				assertFalse(it.hasNext());
			}
		};
		String source = "2010-03-19 14:30:01,341 INFO  [org.quartz.impl.StdSchedulerFactory] Quartz scheduler version: 1.5.2";
		RulesReader rules = getExampleRules();
		testStart(rules, source, asserts);
	}

}
