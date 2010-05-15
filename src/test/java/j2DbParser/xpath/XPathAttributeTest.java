package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class XPathAttributeTest {
	private static final String EXAMPLE_XML = "testXml.xml";

	@Test
	public void single() throws Exception {
		String xpath = "/config/item/@date";
		String[] xpaths = { xpath };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);

		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[January 2009, February 2009, December 2009]", map.get(
				xpath).toString());
		map.removeAll(xpath);
		assertEquals(0, map.size());
	}

	@Test
	public void singleFar() throws Exception {
		String xpath = "/config/item/fun/hah/@id";
		String[] xpaths = { xpath };
		String file = EXAMPLE_XML;
		ListMultimap<String, String> map = parse(xpaths, file);

		if (false) {
			for (Entry<String, String> entry : map.entries()) {
				System.out.println(entry);
			}
		}
		assertEquals("[bla]", map.get(xpath).toString());
		map.removeAll(xpath);
		assertEquals(0, map.size());
	}

	@Test
	public void moreThanOneAt() throws Exception {
		String xpath = "/configSingleton/item/fun/@hah/@id";
		String[] xpaths = { xpath };
		String file = EXAMPLE_XML;
		try {
			parse(xpaths, file);
			fail("moreThanOneAt()");
		} catch (IllegalArgumentException e) {
			assertEquals("xpath can only have one @, it was " + xpath, e
					.getMessage());
		}
	}

	private ArrayListMultimap<String, String> parse(String[] xpaths, String file)
			throws IOException, XMLStreamException {
		return ArrayListMultimap.create(new XPathStaXParser().parse(file,
				xpaths));
	}

}
