package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.cli.ParserLive;

public class ParserRunner {
	public static void main(String[] args) throws Exception {
		args = EOptions.example(FILE, RULE_NAME);
		if (true) {
			new ParserLive().start(args);
		} else {
			Parser.init(args);
		}
	}

}
