package j2DbParser.io;

import j2DbParser.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Reads rules
 */
// TODO: commons configuration
public class RulesReader {

	private final String rulesFile;
	private Map<String, String> rulesMap;

	public RulesReader(String rulesFile) throws IOException {
		this.rulesFile = rulesFile;
	}

	private Map readRules(String rulesFile) {
		try {
			return FileUtils.file2Map(rulesFile);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(0);
			return null;
		}
	}

	public Map<String, String> getRulesMap() {
		if (rulesMap == null) {
			rulesMap = readRules(rulesFile);
		}
		return rulesMap;
	}

}
