package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import com.google.common.collect.ListMultimap;

public class SOObserverTest {
	private static final String EXAMPLE_XML = "testSo1.xml";

	@Test
	public void single() throws Exception {
		String xpathRep = "/users/row/@Reputation";
		String xpathId = "/users/row/@Id";
		String[] xpaths;

		xpaths = new String[] { xpathId, xpathRep };
		List<NodeTO> parse = parse(xpaths, EXAMPLE_XML);
		Iterator<NodeTO> it = parse.iterator();
		NodeTO next;

		next = it.next();
		assertEquals("IdValue", next.data);
		assertEquals("/users/row/@Id", next.xpath);
		next = it.next();
		assertEquals("ReputationValue", next.data);
		assertEquals("/users/row/@Reputation", next.xpath);
		assertFalse(it.hasNext());

		xpaths = new String[] { xpathRep, xpathId };
		parse = parse(xpaths, EXAMPLE_XML);
		it = parse.iterator();

		next = it.next();
		assertEquals("ReputationValue", next.data);
		assertEquals("/users/row/@Reputation", next.xpath);
		next = it.next();
		assertEquals("IdValue", next.data);
		assertEquals("/users/row/@Id", next.xpath);
		assertFalse(it.hasNext());
	}

	static void gen(List<NodeTO> parse) {
		for (NodeTO nodeTO : parse) {
			if (nodeTO.equals(parse.get(0))) {
				System.out
						.println("Iterator < NodeTO > it = parse.iterator();\nNodeTO next;\n");
			}
			System.out.println("next = it.next();");

			System.out.println("assertEquals(\"" + nodeTO.data + "\", next."
					+ "data" + ");");
			System.out.println("assertEquals(\"" + nodeTO.xpath + "\", next."
					+ "xpath" + ");");
		}
	}

	private List<NodeTO> parse(String[] xpaths, String file)
			throws IOException,
			XMLStreamException {
		XPathStaXParser parser = new XPathStaXParser();
		final List<NodeTO> list = new ArrayList<NodeTO>();
		parser.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				NodeTO to = (NodeTO) arg;
				// System.out.println("update(" + arg + ")");
				list.add(to);
			}
		});
		ListMultimap<String, String> parse = parser.parse(file, xpaths);
		assertEquals(0, parse.size());
		return list;
	}

}
