package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.ConfigSingleton;
import j2DbParser.cli.CommandLineSupport;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.guice.GuiceInjector;
import j2DbParser.system.ISystemEnv;
import j2DbParser.system.StopperDecorator;

public class StackoverflowRunner {

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

		public String[] asArgs(String dir) {
			return EOptions.example(FILE.setParamValue(dir + filename),
					RULE_NAME.setParamValue(sectionName));
		}

	}

	public static void main(String[] args) throws Exception {
		GuiceInjector.getInstance(ConfigSingleton.class).maxInsertCount = 5;

		final String dir = ISystemEnv.SO_DUMP_DIR;
		if (dir == null) {
			throw new IllegalStateException("dir is null");
		}
		System.out.println("dir=" + dir);
		if (false) {
			for (ESoTableType e : ESoTableType.values()) {
				run(e, dir);
			}
			// run(ESoTableType.POSTS);
		} else {
			// all at once
			new StopperDecorator("all at once") {
				@Override
				protected void watch() throws Exception {
					for (ESoTableType e : ESoTableType.values()) {
						String[] args = asSingle(dir, e);
						Parser.main(args);
					}
				}
			};
		}

		for (ESoTableType e : ESoTableType.values()) {
			System.out.println(CommandLineSupport.asCommandLineArgs(asSingle(
					dir, e)));
		}
	}

	private static void run(ESoTableType now, String dir) throws Exception {
		final String[] args = now.asArgs(dir);
		new StopperDecorator(now.name()) {
			@Override
			protected void watch() throws Exception {
				Parser.main(args);
			}
		};
	}

	public static String[] asSingle(final String dir, ESoTableType e) {
		String[] args = EOptions.example(FILE.setParamValue(dir + e.filename),
				RULE_NAME.setParamValue("SO"));
		return args;
	}

}
