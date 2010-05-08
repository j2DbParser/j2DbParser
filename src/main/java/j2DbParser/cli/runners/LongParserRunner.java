package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.cli.ParserLive;

public class LongParserRunner {
	public static void main(String[] args) throws Exception {
		String bigLogFile = "c:\\jboss-EMIL\\server\\intgr\\log2\\server.log.2010-03-19";
		args = EOptions.example(FILE.setParamValue(bigLogFile), RULE_NAME);
		if (true) {
			new ParserLive().start(args);
		} else {
			Parser.init(args);
		}
	}
}
