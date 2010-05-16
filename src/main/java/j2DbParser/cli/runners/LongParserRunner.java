package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.cli.ParserLive;
import j2DbParser.system.ISystemEnv;

public class LongParserRunner {
	public static void main(String[] args) throws Exception {
		String filename = ISystemEnv.JBOSS_LOG_DIR + "\\server.log.2010-03-19";
		args = EOptions.example(FILE.setParamValue(filename), RULE_NAME);
		if (true) {
			new ParserLive().start(args);
		} else {
			Parser.init(args);
		}
	}
}
