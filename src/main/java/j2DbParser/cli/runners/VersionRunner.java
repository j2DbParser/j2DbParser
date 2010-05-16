package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.VERSION;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.cli.ParserLive;

public class VersionRunner {
	public static void main(String[] args) throws Exception {
		args = EOptions.example(VERSION);

		Parser.init(args);
		new ParserLive().start(args);
	}

}
