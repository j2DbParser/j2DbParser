package j2DbParser.system;

import org.apache.commons.lang.time.DurationFormatUtils;

/**
 * Shows how long execution of method took
 */
public abstract class StopperDecorator {
	/**
	 * method to watch
	 * 
	 * @throws Exception
	 */
	abstract protected void watch() throws Exception;

	public StopperDecorator(String name) throws Exception {
		long startTime = System.currentTimeMillis();
		try {
			watch();
		} finally {
			System.out.println("\""
					+ name
					+ "\""
					+ " took "
					+ DurationFormatUtils.formatDurationHMS(System
							.currentTimeMillis()
							- startTime) + " ms\n");
		}
	}
}
