package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

public class NodeListNodeListTest {

	@Test
	public void relative() {
		try {
			new NodeList("/aaa/b");
			fail("relative()");
		} catch (IllegalArgumentException e) {
			assertEquals("xpathExpre must starts with \"//\", it was /aaa/b", e
					.getMessage());
		}
	}

	@Test
	public void empty() {
		try {
			new NodeList("//");
			fail("empty()");
		} catch (IllegalArgumentException e) {
			assertEquals("nodeArrayLength must be larger than 0", e
					.getMessage());
		}
	}

	@Test
	public void single() {
		NodeList nodeList = new NodeList("//aaa");
		Iterator<Node> it = nodeList.iterator();

		assertEquals("aaa", it.next().toString());
		assertFalse(it.hasNext());
	}

	@Test
	public void two() {
		NodeList nodeList = new NodeList("//aaa/b");
		Iterator<Node> it = nodeList.iterator();

		assertEquals("aaa", it.next().toString());
		assertEquals("b", it.next().toString());
		assertFalse(it.hasNext());
	}

	@Test
	public void three() {
		NodeList nodeList = new NodeList("//a/b/c");
		Iterator<Node> it = nodeList.iterator();

		assertEquals("a", it.next().toString());
		assertEquals("b", it.next().toString());
		assertEquals("c", it.next().toString());
		assertFalse(it.hasNext());
	}
}
