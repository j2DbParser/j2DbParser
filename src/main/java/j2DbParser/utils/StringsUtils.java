package j2DbParser.utils;

import org.apache.commons.lang.StringUtils;

public final class StringsUtils {

	public static String wordJoiner(String text, String between) {
		if (between == null || between.length() <= 0) {
			throw new IllegalArgumentException("between must be larger than 0");
		}
		String[] split = text.split(between);
		if (split.length > 1) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				if (i == 0) {
					sb.append(StringUtils.uncapitalize(string));
				} else {
					sb.append(StringUtils.capitalize(string));
				}
			}
			return sb.toString();
		}
		return text;
	}

}