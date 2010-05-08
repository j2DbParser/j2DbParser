package j2DbParser.io;

import j2DbParser.system.StopperSingleton;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;

/**
 * Reads rules from ini
 */
public class RulesReader {

	private static final String RULES_FILE = "rules.ini";
	private final String ruleName;
	private Map<String, String> rulesMap;

	public RulesReader(String ruleName) throws IOException {
		this.ruleName = ruleName;
	}

	private Map readRules(String sectionName) {
		try {
			Wini ini = new Wini(new File(RULES_FILE));
			Section section = ini.get(sectionName);
			if (section == null) {
				StopperSingleton.getInstance().stop(
						sectionName + " section not found in "
						+ RULES_FILE);
			}
			return section;
		} catch (Exception e) {
			StopperSingleton.getInstance().stop(e);
			return null;
		}
	}

	public Map<String, String> getRulesMap() {
		if (rulesMap == null) {
			rulesMap = readRules(ruleName);
		}
		return rulesMap;
	}
}
