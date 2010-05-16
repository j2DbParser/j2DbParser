package j2DbParser.xpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class NodeList implements Iterable<Node> {
	private static final int SPLIT_BEGIN_IGNORE_ELEMENTS = 1;
	private static final boolean DEBUG_IS = false;
	public static final boolean DEBUG_SPLIT = false;
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
		if (xpathExpr.startsWith("//")) {
			throw new IllegalArgumentException(
					"xpathExpre should not start with \"//\", it was "
							+ xpathExpr);
		}
		this.xpath = xpathExpr;
		String[] split = xpathExpr.split("/");

		int length = split.length;
		int nodeArrayLength = length - SPLIT_BEGIN_IGNORE_ELEMENTS;
		if (nodeArrayLength <= 0) {
			throw new IllegalArgumentException(
					"nodeArrayLength must be larger than 0");
		}
		nodes = new Node[nodeArrayLength];

		if (DEBUG_SPLIT) {
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				System.out.println(i + ")" + string);
			}
		}

		int j = 0;
		for (int i = SPLIT_BEGIN_IGNORE_ELEMENTS; i < length; i++) {
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
		boolean equals = Arrays.equals(array, as);
		if (DEBUG_IS) {
			System.out.println("nodeNameArray=" + Arrays.toString(as));
			System.out.println("array=" + Arrays.toString(array));
			System.out.println("equals=" + equals);
		}
		return equals;
	}

}
