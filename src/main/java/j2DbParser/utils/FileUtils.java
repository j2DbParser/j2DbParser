package j2DbParser.utils;

import j2DbParser.system.ISystemEnv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.apache.commons.lang.StringUtils;

public class FileUtils {
	private static final int DEFAULT_LIST_READ_CAPACITY = 50000;
	private static final String ENCODING = "UTF-8";

	public static Map<String, String> file2Map(String filename)
			throws FileNotFoundException {
		final List<String> list = file2List(filename, ENCODING);
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

	public static List<String> file2List(final String filename,
			final String codepage) throws FileNotFoundException {
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
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
	}

	public static String fileSelect() {
		final JFileChooser fc = new JFileChooser(new File(ISystemEnv.START_DIR));
		fc.showOpenDialog(null);
		File selectedFile = fc.getSelectedFile();
		String canonicalPath = null;
		if (selectedFile != null) {
			try {
				canonicalPath = selectedFile.getCanonicalPath();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		if (canonicalPath == null) {
			throw new IllegalStateException("file not selected");
		}
		return canonicalPath;
	}
}
