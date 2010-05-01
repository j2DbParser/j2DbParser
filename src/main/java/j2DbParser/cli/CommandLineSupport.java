package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULES_FILE;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Support for command line arguments using Commons CLI. Arguments are stored as
 * enum {@link EOptions}.
 * 
 * @see http://commons.apache.org/cli/
 */
public class CommandLineSupport {
	private final String exeName;
	private final String[] args;

	public CommandLineSupport(String exeName, String[] args) {
		this.exeName = exeName;
		this.args = args;
	}

	/**
	 * Parse command line arguments.
	 * 
	 * @return true if you should not continue
	 */
	public boolean parse() {
		// System.out.println("main(" + Arrays.toString(args) + ")");
		Options options = getOptions();
		if (args.length == 0) {
			new HelpFormatter().printHelp(exeName, options, true);
			return true;
		}
		try {
			EOptions.commandLine = getCliParser().parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			return true;
		}
		return false;
	}

	protected PosixParser getCliParser() {
		// PosixPaser is great when you need to handle options that are one
		// character long, like the t option in this example.
		return new PosixParser();
	}

	/**
	 * Now both {@link Parser} and {@link ParserLive} have same options.
	 * 
	 * @return options options
	 */
	protected Options getOptions() {
		Options options = new Options();

		// OptionGroup required = new OptionGroup();
		// required.addOption(NAME.getOption());
		// required.setRequired(true);
		// options.addOptionGroup(required);

		EOptions[] op = { FILE, RULES_FILE
		// , TREAT_AS
		};
		for (EOptions e : op) {
			options.addOption(e.getOption());
		}
		return options;
	}

}
