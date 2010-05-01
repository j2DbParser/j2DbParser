package j2DbParser.utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;

public class FileUtils {

	private static final int DEFAULT_LIST_READ_CAPACITY = 50000;
	private static final String ENCODING_ISO = "ISO-8859-2";

	public static Map<String, String> file2Map(String filename) {
		final List<String> list = file2CollectionMockable(filename,
				ENCODING_ISO);
		final Map<String, String> map = new LinkedHashMap<String, String>(list
				.size());
		for (String line : list) {
			if (line.indexOf('=') != -1) {
				line = line.trim();
				String before = StringUtils.substringBefore(line, "=");
				String after = StringUtils.substringAfter(line, "=");
				map.put(before, after);
			}
		}
		return map;
	}

	public static List<String> file2CollectionMockable(final String filename,
			final String codepage) {
		Scanner scan = null;
		try {
			scan = new Scanner(new FileInputStream(filename).getChannel(),
					codepage);
			List<String> list = new ArrayList<String>(
					DEFAULT_LIST_READ_CAPACITY);
			while (scan.hasNextLine()) {
				list.add(scan.nextLine());
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
	}
}
