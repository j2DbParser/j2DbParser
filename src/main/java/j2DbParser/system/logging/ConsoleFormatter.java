package j2DbParser.system.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		Level level = record.getLevel();
		if (level == Level.INFO) {
			return record.getMessage() + "\n";
		}
		return "";
	}

}
