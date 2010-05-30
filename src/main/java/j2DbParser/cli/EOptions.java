package j2DbParser.cli;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
			return "src/test/resources/example.log";
		}
	},
	RULE_NAME("r") {
		@Override
		public Option getOption() {
			return OptionBuilder.withArgName("ruleName").hasArg()
					.withDescription("name of section in rules.ini")
					.withLongOpt("rule").isRequired(true).create(name);
		}

		@Override
		public String exampleValue() {
			return "log"; // ini section
		}
	},
	VERSION("v") {
		@Override
		public Option getOption() {
			return OptionBuilder.withDescription("version of application")
					.withLongOpt("version").isRequired(false).create(name);
		}

		@Override
		public String exampleValue() {
			return null;
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
	// TODO: stuff from ConfigSingleton
	;
	public static CommandLine commandLine;
	public static Set<EOptions> chosen;
	public static EFileType type;
	public final String name;
	private String paramValue;

	private EOptions(String name) {
		this.name = name;
	}

	abstract public Option getOption();

	public String value() {
		if (chosen == null) {
			chosen = getChosenOptions();
		}
		return commandLine.getOptionValue(name);
	}

	private Set<EOptions> getChosenOptions() {
		Set<EOptions> set = new HashSet<EOptions>();
		@SuppressWarnings("unchecked")
		Iterator<Option> iterator = commandLine.iterator();
		while (iterator.hasNext()) {
			Option next = iterator.next();
			for (EOptions e : values()) {
				if (e.getOption().equals(next)) {
					set.add(e);
				}
			}
		}
		return set;
	}

	public Integer valueInt() {
		String value = value();
		if (value != null) {
			return Integer.parseInt(value);
		}
		return null;
	}

	public boolean has() {
		if (chosen == null) {
			chosen = getChosenOptions();
		}
		return chosen.contains(this);
	}

	public String exampleValue() {
		return name + "1";
	}

	public static String[] example(EOptions... options) {
		String[] as = new String[options.length * 2];
		int i = 0;
		int smallDown = 0;
		for (EOptions e : options) {
			as[i++] = "-" + e.name;

			String paramVal = e.paramValue;
			String exampleValue = paramVal != null ? paramVal : e
					.exampleValue();
			if (exampleValue != null) {
				as[i++] = exampleValue;
			} else {
				smallDown++;
			}
		}
		if (smallDown > 0) {
			as = Arrays.copyOf(as, as.length - smallDown);
		}
		return as;
	}

	public static void show() {
		for (EOptions e : values()) {
			System.out.println(e);
			System.out.println("\t" + e.value());
		}
	}

	public EOptions setParamValue(String paramValue) {
		this.paramValue = paramValue;
		return this;
	}
}
