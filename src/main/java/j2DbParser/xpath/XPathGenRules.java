package j2DbParser.xpath;

import j2DbParser.utils.FileUtils;

import org.apache.commons.lang.StringUtils;

public class XPathGenRules extends XPathGen {
	public static void main(String[] args) throws Exception {
		String file = "testSo1.xml";
		file = FileUtils.fileSelect();
		new XPathGenRules().start(file);
	}

	@Override
	protected void addAttribute(StringBuilder sb, String attrName) {
		sb.append("/").append("@").append(attrName);
	}

	@Override
	protected String getXpath(String attrName) {
		String xpath = super.getXpath(attrName);
		String table = StringUtils.substringBetween(xpath, "/", "/");
		// System.out.println("table=" + table);
		return table + "." + attrName + "=" + xpath;
	}
}
