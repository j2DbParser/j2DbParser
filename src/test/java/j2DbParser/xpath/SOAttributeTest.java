package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class SOAttributeTest {
	private static final String EXAMPLE_XML = "testSo.xml";

	@Test
	public void single() throws Exception {
		String xpath = "/users/row/@Reputation";
		String xpath2 = "/users/row/@Id";
		String[] xpaths = { xpath, xpath2 };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);

		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[1, 2]", map.get(xpath).toString());
		map.removeAll(xpath);

		assertEquals("[-1, -3]", map.get(xpath2).toString());
		map.removeAll(xpath2);
		assertEquals(0, map.size());
	}

	private ArrayListMultimap<String, String> parse(String[] xpaths, String file)
			throws IOException, XMLStreamException {
		file = ClassLoader.getSystemResource(file).getFile();
		return ArrayListMultimap.create(new XPathStaXParser().parse(file,
				xpaths));
	}

}
