package j2DbParser.cli.runners;

import static j2DbParser.cli.EOptions.FILE;
import static j2DbParser.cli.EOptions.RULE_NAME;
import j2DbParser.cli.EOptions;
import j2DbParser.cli.Parser;
import j2DbParser.system.ISystemEnv;

public class IniLogParserRunner {
	public static void main(String[] args) throws Exception {
		String filename = ISystemEnv.JBOSS_LOG_DIR + "\\server.log";
		args = EOptions.example(FILE.setParamValue(filename), RULE_NAME);
		Parser.main(args);
	}
}
