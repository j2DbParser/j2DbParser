package j2DbParser.xpath;

import j2DbParser.utils.IterableDecorator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

public class XPathStaXParser {

	private List<NodeList> inList;
	private ArrayListMultimap<String, String> outMap;

	private final LinkedList<String> locationList = new LinkedList<String>();
	private List<NodeList> chosenNodeList;

	private void action(InputStream in, List<NodeList> list,
			ArrayListMultimap<String, String> map) throws XMLStreamException,
			IOException {
		this.inList = list;
		this.outMap = map;
		try {
			XMLEventReader eventReader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);
			for (XMLEvent e : new IterableDecorator<XMLEvent>(eventReader)) {
				parse(e);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public ListMultimap<String, String> parse(String file, String... xpaths)
			throws IOException, XMLStreamException {
		List<NodeList> list = new ArrayList<NodeList>();
		for (String xpath : xpaths) {
			NodeList nList = new NodeList(xpath);
			list.add(nList);
		}

		ArrayListMultimap<String, String> map = ArrayListMultimap.create(
				xpaths.length, 10);

		InputStream stream = null;
		try {
			stream = ClassLoader.getSystemResourceAsStream(file);
			if (stream == null) {
				throw new IllegalArgumentException(
						"file points to not existing resource,it was=" + file);
			}
			new XPathStaXParser().action(stream, list, map);

		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return Multimaps.unmodifiableListMultimap(map);
	}

	private void parse(XMLEvent e) {
		if (e.isStartElement()) {
			chosenNodeList = new ArrayList<NodeList>();

			StartElement start = e.asStartElement();
			String tagName = start.getName().getLocalPart();
			// System.out.println("start=" + tagName);
			locationList.add(tagName);
			for (NodeList nodeList : inList) {
				if (check(nodeList)) {
					chosenNodeList.add(nodeList);
				}
			}

			if (!chosenNodeList.isEmpty()) {
				IterableDecorator<Attribute> it = new IterableDecorator<Attribute>(
						start.getAttributes());

				for (Iterator<NodeList> itCh = chosenNodeList.iterator(); itCh
						.hasNext();) {
					NodeList nodeList = itCh.next();
					if (checkAttribute(nodeList, it, outMap)) {
						itCh.remove();
					}
				}
			}

			if (false) {
				Iterator<Attribute> attributes = start.getAttributes();
				for (Attribute a : new IterableDecorator<Attribute>(attributes)) {
					String value = a.getValue();
					System.out.println("a.value=" + value);
				}
			}
		}

		if (e.isEndElement()) {
			if (false) {
				EndElement end = e.asEndElement();
				String tagName = end.getName().getLocalPart();
				System.out.println("end=" + tagName);
			}
			locationList.removeLast();
		}

		// System.out.println(locationList);
		Characters cha;
		if (e.isCharacters() && !(cha = e.asCharacters()).isWhiteSpace()) {
			String data = cha.getData();
			List<NodeList> lnl = chosenNodeList;
			if (!lnl.isEmpty()) {
				// System.out.println("data=" + data);
				for (NodeList nodeList : lnl) {
					if (nodeList.attribute == null) {
						outMap.put(nodeList.xpath, data);
					}
				}
			}
		}

		if (false) {
			if (e.isEndDocument()) {
				System.out.println("e.isEndDocument()=" + e.isEndDocument());
			}
		}
	}

	private boolean checkAttribute(NodeList nodeList,
			IterableDecorator<Attribute> it,
			ArrayListMultimap<String, String> outMap2) {
		String attr = nodeList.attribute;
		// System.out.println("attr=" + attr);
		for (Attribute attribute : it) {
			String realAttrName = attribute.getName().getLocalPart();
			// System.out.println("realAttr=" + realAttrName);
			if (realAttrName.equalsIgnoreCase(attr)) {
				outMap.put(nodeList.xpath, attribute.getValue());
				return true;
			}
		}
		return false;
	}

	private boolean check(NodeList nodeList) {
		return (nodeList.is(locationList));
	}

}
