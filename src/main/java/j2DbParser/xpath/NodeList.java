package j2DbParser.xpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class NodeList implements Iterable<Node> {
	public Node[] nodes;
	private int current;
	public final String xpath;
	private final String[] as;
	public String attribute;

	// public Node current;

	public NodeList(String xpathExpr) throws IllegalArgumentException {
		if (xpathExpr == null) {
			throw new IllegalArgumentException("xpathExpre must not be null");
		}
		if (!xpathExpr.startsWith("//")) {
			throw new IllegalArgumentException(
					"xpathExpre must starts with \"//\", it was " + xpathExpr);
		}
		this.xpath = xpathExpr;
		String[] split = xpathExpr.split("/");

		int length = split.length;
		int nodeArrayLength = length - 2;
		if (nodeArrayLength <= 0) {
			throw new IllegalArgumentException(
					"nodeArrayLength must be larger than 0");
		}
		nodes = new Node[nodeArrayLength];
		int j = 0;
		for (int i = 2; i < length; i++) {
			String s = split[i];
			boolean attr = false;
			if (s.startsWith("@")) {
				if (attribute != null) {
					throw new IllegalArgumentException(
							"xpath can only have one @, it was " + xpathExpr);
				}

				s = s.substring(1);
				attribute = s;
				attr = true;
			}
			// System.out.println("s=" + s);
			nodes[j++] = new Node(s, attr);
		}
		as = asArray();
		// System.out.println("as=" + Arrays.toString(as));
	}

	protected String[] asArray() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < nodes.length; i++) {
			Node node = nodes[i];
			if (!node.attr) {
				list.add(node.name);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			public boolean hasNext() {
				return current < nodes.length - 1;
			}

			public Node next() {
				return nodes[current++];
			}

			public void remove() {
				throw new NotImplementedException();
			}
		};
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.NO_FIELD_NAMES_STYLE);
	}

	public boolean is(LinkedList<String> location) {
		String[] array = location.toArray(new String[location.size()]);
		if (false) {
			System.out.println("nodeNameArray=" + Arrays.toString(as));
			System.out.println("array=" + Arrays.toString(array));
		}
		if (Arrays.equals(array, as)) {
			// System.out.println("--------------");
			return true;
		}
		return false;
	}

}
