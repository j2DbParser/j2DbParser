package j2DbParser.cli;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import static j2DbParser.cli.EOptions.VERSION;
import j2DbParser.system.StopperSingleton;

import java.util.Arrays;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionGroup;
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
	public void parse() {
		System.out.println("main(" + Arrays.toString(args) + ")");
		Options options = getOptions();
		if (args.length == 0) {
			new HelpFormatter().printHelp(exeName, options, true);
			stop();
		}
		try {
			EOptions.commandLine = getCliParser().parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			stop();
		}
		// options.hasOption("v")

		checkGroups();
	}

	private void checkGroups() {
		if (VERSION.has()) {
			// TODO: finish
			String v = "Parser 0.0.3 (2010-05-08 19:48:00)";
			System.out.println(v);
			stop();
		}
	}

	private void stop() {
		StopperSingleton.getInstance().stop();
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
		final Options options = new Options();

		EOptions[] op = { FILE, RULE_NAME };

		ver1(options, op);
		// ver2(options, op);

		// in OptionGroup = mutually exclusive options

		OptionGroup versionGroup = new OptionGroup();
		versionGroup.setRequired(false);
		versionGroup.addOption(VERSION.getOption());

		options.addOptionGroup(versionGroup);
		return options;
	}

	@Deprecated
	public void ver2(final Options options, EOptions[] op) {
		// main([-f, example.log, -r, example])
		// The option 'r' was specified but an option from this group has
		// already been selected: 'f'
		OptionGroup group = new OptionGroup();
		group.setRequired(false);
		for (EOptions e : op) {
			group.addOption(e.getOption());
		}
		options.addOptionGroup(group);
	}

	private void ver1(final Options options, EOptions[] op) {
		for (EOptions e : op) {
			OptionGroup group = new OptionGroup();
			group.setRequired(false);
			group.addOption(e.getOption());
			options.addOptionGroup(group);
		}
	}

}
