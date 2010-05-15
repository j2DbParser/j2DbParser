package j2DbParser.xpath;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

abstract public class XmlObserver implements Observer {

	private int count;
	private final Map<String, String> map = new LinkedHashMap<String, String>();

	public void update(Observable o, Object arg) {
		NodeTO to = (NodeTO) arg;
		String key = to.xpath;
		if (map.get(key) != null) {
			try {
				boolean stop = updateLine(map);
				if (stop) {
					XPathStaXParser x = (XPathStaXParser) o;
					x.setStop(true);
					return;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			count++;
			map.clear();
		}
		map.put(key, to.data);
	}

	/**
	 * @param map
	 * @return stop processing
	 * @throws Exception
	 */
	abstract public boolean updateLine(Map<String, String> map)
			throws Exception;

	public int getCount() {
		return count;
	}

}
