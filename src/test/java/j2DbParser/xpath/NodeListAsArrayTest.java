package j2DbParser.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NodeListAsArrayTest {
	@Test
	public void asArray() throws Exception {
		String[] asArray = new NodeList("//config/item").asArray();

		int i = 0;
		assertEquals("config", asArray[i++]);
		assertEquals("item", asArray[i++]);
		assertTrue(i == asArray.length);
	}
}
