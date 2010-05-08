package j2DbParser.xpath;

public class Node {
	public String name;
	public boolean attribute;
	public final boolean attr;

	public Node(String name, boolean attr) {
		super();
		this.name = name;
		this.attr = attr;
	}

	@Override
	public String toString() {
		return name;
	}

}
