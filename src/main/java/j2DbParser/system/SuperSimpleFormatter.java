package j2DbParser.system;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class SuperSimpleFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		Level level = record.getLevel();
		if (level == Level.INFO
		// || level == Level.WARNING

		) {
			return record.getMessage() + "\n";
		}
		return "";
	}

}
