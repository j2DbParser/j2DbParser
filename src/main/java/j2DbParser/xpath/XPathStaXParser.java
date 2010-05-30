package j2DbParser.xpath;

import j2DbParser.utils.IterableDecorator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.xml.stream.FactoryConfigurationError;
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

@SuppressWarnings("unchecked")
public class XPathStaXParser extends Observable {
	public static final boolean T = true;
	public static final boolean F = false;

	private static final boolean DEBUG_CHOSEN = F;
	private static final boolean DEBUG_ATTR_FINDER = F;
	public static final boolean DEBUG_TAGS = F;
	private static final boolean DEBUG_LOCATION = F;

	private List<NodeList> inList;
	private ArrayListMultimap<String, String> outMap;

	private final LinkedList<String> locationList = new LinkedList<String>();
	private List<NodeList> chosenNodeList;

	private boolean stop;

	private void prepareReadXml(InputStream in, List<NodeList> list,
			ArrayListMultimap<String, String> map) throws XMLStreamException,
			IOException {
		this.inList = list;
		this.outMap = map;
		readXml(in);
	}

	protected void readXml(InputStream in) throws XMLStreamException,
			FactoryConfigurationError, IOException {
		try {
			XMLEventReader eventReader = XMLInputFactory.newInstance()
					.createXMLEventReader(in);
			for (XMLEvent e : new IterableDecorator<XMLEvent>(eventReader)) {
				if (parse(e)) {
					break;
				}
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

			File file2 = new File(file);
			String canonicalPath = file2.getCanonicalPath();
			if (DEBUG_TAGS) {
				System.out.println("canonicalPath=" + canonicalPath);
			}
			stream = new FileInputStream(file);
			prepareReadXml(stream, list, map);
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return Multimaps.unmodifiableListMultimap(map);
	}

	/**
	 * 
	 * 
	 * @param e
	 * @return stop processing
	 */
	private boolean parse(XMLEvent e) {
		if (e.isStartElement()) {
			chosenNodeList = new ArrayList<NodeList>();

			StartElement start = e.asStartElement();
			String tagName = start.getName().getLocalPart();

			if (DEBUG_TAGS) {
				System.out.println("start=" + tagName);
			}
			locationList.add(tagName);
			processingStartElement(start, locationList);
		}

		if (e.isEndElement()) {
			if (DEBUG_TAGS) {
				EndElement end = e.asEndElement();
				String tagName = end.getName().getLocalPart();
				System.out.println("end=" + tagName);
			}
			locationList.removeLast();
		}

		processingLocation(locationList);

		Characters cha;
		if (e.isCharacters() && !(cha = e.asCharacters()).isWhiteSpace()) {
			String data = cha.getData();
			processingTextElement(data);
		}

		if (DEBUG_TAGS) {
			if (e.isEndDocument()) {
				System.out.println("e.isEndDocument()=" + e.isEndDocument());
			}
		}
		return stop;
	}

	protected void processingLocation(LinkedList<String> location) {
		if (DEBUG_LOCATION) {
			System.out.println(location);
		}
	}

	protected void processingTextElement(String data) {
		List<NodeList> lnl = chosenNodeList;
		if (!lnl.isEmpty()) {
			// System.out.println("data=" + data);
			for (NodeList nodeList : lnl) {
				if (nodeList.attribute == null) {
					setChanged();
					notifyObservers(new NodeTO(nodeList.xpath, data));
					if (countObservers() == 0) {
						outMap.put(nodeList.xpath, data);
					}
				}
			}
		}
	}

	protected void processingStartElement(StartElement start,
			LinkedList<String> location) {
		for (NodeList nodeList : inList) {
			if (check(nodeList)) {
				chosenNodeList.add(nodeList);
				if (DEBUG_CHOSEN) {
					System.out.println("chosenNodeList=" + chosenNodeList);
				}
			}
		}

		if (!chosenNodeList.isEmpty()) {
			for (Iterator<NodeList> itCh = chosenNodeList.iterator(); itCh
					.hasNext();) {
				Iterable<Attribute> it = IterableDecorator.create(start
						.getAttributes());
				NodeList nodeList = itCh.next();
				if (checkAttribute(nodeList, it)) {
					itCh.remove();
				}
			}
		}

		if (DEBUG_TAGS) {
			Iterator<Attribute> attributes = start.getAttributes();
			for (Attribute a : IterableDecorator.create(attributes)) {
				String value = a.getValue();
				System.out.println("a.value=" + value);
			}
		}
	}

	private boolean checkAttribute(NodeList nodeList, Iterable<Attribute> it) {
		String attr = nodeList.attribute;
		if (DEBUG_ATTR_FINDER) {
			System.out.println("attr=" + attr);
		}
		for (Attribute attribute : it) {
			String realAttrName = attribute.getName().getLocalPart();
			if (DEBUG_ATTR_FINDER) {
				System.out.println("realAttr=" + realAttrName);
			}
			if (realAttrName.equalsIgnoreCase(attr)) {
				String value = attribute.getValue();
				String xpath = nodeList.xpath;
				if (DEBUG_ATTR_FINDER) {
					System.out.println("outMap.put(" + xpath + ", " + value
							+ ");");
				}
				setChanged();
				notifyObservers(new NodeTO(nodeList.xpath, value));
				if (countObservers() == 0) {
					outMap.put(xpath, value);
				}
				return true;
			}
		}
		return false;
	}

	private boolean check(NodeList nodeList) {
		return (nodeList.is(locationList));
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
