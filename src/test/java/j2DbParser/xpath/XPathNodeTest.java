package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class XPathNodeTest {
	private static final String EXAMPLE_XML = "testXml.xml";

	@Test
	public void single() throws Exception {
		String xpath = "/config/item/current";
		String[] xpaths = { xpath };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);

		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[1, 2, 100]", map.get(xpath).toString());
		map.removeAll(xpath);
		assertEquals(0, map.size());
	}

	@Test
	public void tripleSameParent() throws Exception {
		String xpathStart = "/config/item/";
		String xpath1 = xpathStart + "current";
		String xpath2 = xpathStart + "interactive";
		String xpath3 = xpathStart + "unit";
		String[] xpaths = { xpath1, xpath2, xpath3 };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);
		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[1, 2, 100]", map.get(xpath1).toString());
		map.removeAll(xpath1);
		assertEquals("[1, 5, 3]", map.get(xpath2).toString());
		map.removeAll(xpath2);
		assertEquals("[900, 400, 5]", map.get(xpath3).toString());
		map.removeAll(xpath3);

		assertEquals(0, map.size());
	}

	@Test
	public void tripleOneFromOtherParent() throws Exception {
		String xpathStart = "/config/item/";
		String xpath1 = xpathStart + "current";
		String xpath2 = xpathStart + "fun/hah";
		String xpath3 = xpathStart + "unit";
		String[] xpaths = { xpath1, xpath2, xpath3 };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);
		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[1, 2, 100]", map.get(xpath1).toString());
		map.removeAll(xpath1);
		assertEquals("[funny1, funny2]", map.get(xpath2).toString());
		map.removeAll(xpath2);
		assertEquals("[900, 400, 5]", map.get(xpath3).toString());
		map.removeAll(xpath3);

		assertEquals(0, map.size());
	}

	@Test
	public void notExistingResource() throws Exception {
		String xpath = "/config/item/current";
		String[] xpaths = { xpath };
		String file = "aaaaa.xml";
		try {
			new XPathStaXParser().parse(file, xpaths);
			fail("notExistingResource()");
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage().indexOf(file) != -1);
		}
	}

	private ArrayListMultimap<String, String> parse(String[] xpaths, String file)
			throws IOException, XMLStreamException {
		return ArrayListMultimap.create(new XPathStaXParser().parse(file,
				xpaths));
	}

}
