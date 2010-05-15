package j2DbParser.xpath;

import j2DbParser.utils.FileUtils;
import j2DbParser.utils.IterableDecorator;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class XPathGen extends XPathStaXParser {
	public static void main(String[] args) throws Exception {
		String file = "testSo1.xml";
		file = FileUtils.fileSelect();
		new XPathGen().start(file);
	}

	public void start(String file) throws Exception {
		FileInputStream in = new FileInputStream(file);
		readXml(in);
	}

	private final Set<String> found = new HashSet<String>();
	private LinkedList<String> location;

	@Override
	protected void processingStartElement(StartElement start,
			LinkedList<String> location) {
		this.location = location;
		if (false) {
			System.out.println("processingStartElement(" + start + ", "
					+ location + ")");
		}

		for (Attribute a : new IterableDecorator<Attribute>(start
				.getAttributes())) {
			String xpath = getXpath(a.getName().getLocalPart());
			if (found.add(xpath)) {
				System.out.println(xpath);
			}
		}
	}

	protected String getXpath(String attrName) {
		StringBuilder sb = new StringBuilder();
		String ad = "/";
		for (String s : location) {
			sb.append(ad);
			sb.append(s);
		}
		if (attrName != null) {
			// TODO: change to oldschool xpath?
			addAttribute(sb, attrName);
		}
		return sb.toString();
	}

	protected void addAttribute(StringBuilder sb, String attrName) {
		sb.append("/").append("attribute::").append(attrName);
	}

	@Override
	protected void processingTextElement(String data) {
		String xpath = getXpath(null);
		if (found.add(xpath)) {
			System.out.println(xpath);
		}
	}

	@Override
	protected void processingLocation(LinkedList<String> location) {

	}
}
