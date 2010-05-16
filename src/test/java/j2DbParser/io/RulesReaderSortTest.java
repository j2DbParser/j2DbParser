package j2DbParser.io;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ini4j.Profile.Section;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class RulesReaderSortTest {
	@Test
	public void sort() throws Exception {
		Section section = getObject(Section.class);
		Map<String, String> map = new RulesReader(null).sort(section);
		map = new LinkedHashMap<String, String>(map);

		String key;

		key = "users.Id(INTEGER)";
		assertEquals("/users/row/@Id", map.get(key));
		map.remove(key);
		key = "users.Reputation(INTEGER)";
		assertEquals("/users/row/@Reputation", map.get(key));
		map.remove(key);
		key = "users.CreationDate(TEXT)";
		assertEquals("/users/row/@CreationDate", map.get(key));
		map.remove(key);
		key = "users.DisplayName(TEXT)";
		assertEquals("/users/row/@DisplayName", map.get(key));
		map.remove(key);
		key = "users.EmailHash(TEXT)";
		assertEquals("/users/row/@EmailHash", map.get(key));
		map.remove(key);
		key = "users.LastAccessDate(TEXT)";
		assertEquals("/users/row/@LastAccessDate", map.get(key));
		map.remove(key);
		key = "users.WebsiteUrl(TEXT)";
		assertEquals("/users/row/@WebsiteUrl", map.get(key));
		map.remove(key);
		key = "users.Location(TEXT)";
		assertEquals("/users/row/@Location", map.get(key));
		map.remove(key);
		key = "users.Age(INTEGER)";
		assertEquals("/users/row/@Age", map.get(key));
		map.remove(key);
		key = "users.AboutMe(TEXT)";
		assertEquals("/users/row/@AboutMe", map.get(key));
		map.remove(key);
		key = "users.Views(INTEGER)";
		assertEquals("/users/row/@Views", map.get(key));
		map.remove(key);
		key = "users.UpVotes(INTEGER)";
		assertEquals("/users/row/@UpVotes", map.get(key));
		map.remove(key);
		key = "users.DownVotes(INTEGER)";
		assertEquals("/users/row/@DownVotes", map.get(key));
		map.remove(key);
		assertEquals(0, map.size());

	}

	private <T> T getObject(Class<T> clazz) throws FileNotFoundException,
			IOException {
		InputStream input = null;
		try {
			input = ClassLoader.getSystemResourceAsStream("section.xml");
			Object object = new XStream().fromXML(input);
			return clazz.cast(object);
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
}
