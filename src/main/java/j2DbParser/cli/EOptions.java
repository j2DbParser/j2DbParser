package j2DbParser.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * Console options. single thread only. Arguments are stored in static field
 * {@link EOptions#commandLine}. It might not be the coolest solution, but
 * cooler than passing {@link CommandLine} as method {@link EOptions#value()}
 * parameter.
 */
@SuppressWarnings("static-access")
public enum EOptions {
	FILE("f") {
		@Override
		public Option getOption() {
			return OptionBuilder.withArgName("filename").hasArg()
					.withDescription("file to process").withLongOpt("file")
					.isRequired(true).create(name);
		}

		@Override
		public String exampleValue() {
			return "example.log";
		}
	},
	RULES_FILE("r") {
		@Override
		public Option getOption() {
			return OptionBuilder.withArgName("filename").hasArg()
					.withDescription("processing rules file").withLongOpt(
							"rules").isRequired(true).create(name);
		}

		@Override
		public String exampleValue() {
			return "example.rules.properties";
		}
	},
	// TODO: implement
	TREAT_AS("ta") {
		@Override
		public Option getOption() {
			return OptionBuilder.withArgName("treatAs").hasArg()
					.withDescription("treat as [log]").withLongOpt("type")
					.isRequired(false).create(name);
		}

		@Override
		public String exampleValue() {
			return "log";
		}
	},
	VIEWER_COLUMN_SIZE("cs") {
		@Override
		public Option getOption() {
			return OptionBuilder.withArgName("columnSize").hasArg()
					.withDescription("size of sql column in console")
					.withLongOpt("size").isRequired(false).create(name);
		}

		@Override
		public String exampleValue() {
			return "log";
		}
	},
	// TODO: stuff from Config
	;
	public static CommandLine commandLine;
	public final String name;

	private EOptions(String name) {
		this.name = name;
	}

	abstract public Option getOption();

	public String value() {
		return commandLine.getOptionValue(name);
	}

	public Integer valueInt() {
		String value = value();
		if (value != null) {
			return Integer.parseInt(value);
		}
		return null;
	}

	public boolean has() {
		return value() != null;
	}

	public String exampleValue() {
		return name + "1";
	}

	public static String[] example(EOptions... options) {
		String[] s = new String[options.length * 2];
		int i = 0;
		for (EOptions e : options) {
			s[i++] = "-" + e.name;
			s[i++] = e.exampleValue();
		}
		return s;
	}

	public static void show() {
		for (EOptions e : values()) {
			System.out.println(e);
			System.out.println("\t" + e.value());
		}
	}
}
