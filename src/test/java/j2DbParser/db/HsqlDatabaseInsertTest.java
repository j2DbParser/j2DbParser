package j2DbParser.db;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class HsqlDatabaseInsertTest {

	@Test
	public void getAdded() throws Exception {
		Map<String, String> map = ImmutableMap.of("bla", "one", "haha", "two");
		String insert = new HsqlDatabase().genInsert("bla", map);
		assertEquals("insert into bla\r\n" + "(bla, haha) values \r\n"
				+ "(?, ?);", insert);
	}
}
