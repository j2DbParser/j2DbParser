package j2DbParser.io;

import j2DbParser.system.StopperSingleton;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.ini4j.Wini;
import org.ini4j.Profile.Section;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

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

	private Map<String, String> readRules(String sectionName) {
		try {
			Wini ini = new Wini(new File(RULES_FILE));
			Section section = ini.get(sectionName);
			if (section == null) {
				StopperSingleton.getInstance().stop(
						sectionName + " section not found in " + RULES_FILE);
			}
			// entry is not sorted
			// toString() is sorted

			ImmutableMap<String, String> sorted = sort(section);
			section = null;
			return sorted;
		} catch (Exception e) {
			StopperSingleton.getInstance().stop(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	protected ImmutableMap<String, String> sort(Section section) {
		Field field = findFieldInHierarchy(section, "_impl");

		Map<String, List<String>> map;
		try {
			map = (Map<String, List<String>>) field.get(section);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		Builder<String, String> builder = ImmutableMap.builder();
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			List<String> list = entry.getValue();
			for (String value : list) {
				builder.put(key, value);
			}
		}
		return builder.build();
	}

	private Field findFieldInHierarchy(Object object, String fieldName) {
		Class<?> clazz = object.getClass();
		while (true) {
			// System.out.println("class1=" + class1);
			Field field = null;
			try {
				field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
				// ignore
			}
			clazz = clazz.getSuperclass();
			if (clazz.equals(Object.class)) {
				break;
			}
		}
		return null;
	}

	public Map<String, String> getRulesMap() {
		if (rulesMap == null) {
			rulesMap = readRules(ruleName);
		}
		return rulesMap;
	}
}
