package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;

public class IniParserRunner {
	public static void main(String[] args) throws Exception {
		args = EOptions.example(FILE, RULE_NAME);
		Parser.main(args);
	}
}
