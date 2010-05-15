package j2DbParser.xpath;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class NodeTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String xpath;
	public final String data;

	public NodeTO(String xpath, String data) {
		this.xpath = xpath;
		this.data = data;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
