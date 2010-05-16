package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.system.ISystemEnv;

import java.io.IOException;

import org.apache.commons.lang.time.DurationFormatUtils;

public class SORunner {

	public static enum ESoTableType {
		// 505MB
		// took 0:25:57.911 ms
		// count=6,5mil
		// Data length=208MB

		// 0:31:07.953
		VOTES("SO-votes", "votes.xml"),
		// 57MB
		// took 0:01:12.583 ms
		// count=230 000
		// Data length=31MB
		// 0:01:24.031
		USERS("SO-users", "users.xml"),

		// 54MB
		// took 0:02:42.939 ms
		// count=650 000
		// Data length=33MB
		// 0:03:11.407
		BADGES("SO-badges", "badges.xml"),

		// 569MB
		// took 0:10:45.680 ms
		// count=2,3mil
		// Data length=450MB
		// 0:13:51.390
		COMMENTS("SO-comments", "comments.xml"),

		// code=1118
		// Row size too large. The maximum row size for the used table type, not
		// counting BLOBs, is 65535. You have to change some columns to TEXT or
		// BLOBs
		// typ pola != TEXT

		// 2,5GB
		// took 0:17:41.607 ms
		// count=2,5mil
		// Data length=1,9GB
		POSTS("SO-posts", "posts.xml"), ;

		public final String sectionName;
		public final String filename;

		private ESoTableType(String sectionName, String filename) {
			this.sectionName = sectionName;
			this.filename = filename;
		}
	}

	public static void main(String[] args) throws Exception {
		for (ESoTableType e : ESoTableType.values()) {
			run(e);
		}
		// run(ESoTableType.POSTS);
	}

	private static void run(ESoTableType now) throws Exception, IOException {
		String dir = ISystemEnv.SO_DUMP_DIR;
		if (dir == null) {
			throw new IllegalStateException("dir is null");
		}
		System.out.println("dir=" + dir);
		String[] args;
		args = EOptions.example(FILE.setParamValue(dir + now.filename),
				RULE_NAME.setParamValue(now.sectionName));
		long startTime = System.currentTimeMillis();
		try {
			Parser.init(args);
		} finally {
			System.out.println(now
					+ " took "
					+ DurationFormatUtils.formatDurationHMS(System
							.currentTimeMillis()
							- startTime) + " ms\n");
		}
	}
}
