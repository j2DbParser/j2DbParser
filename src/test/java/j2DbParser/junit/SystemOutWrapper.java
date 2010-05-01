package j2DbParser.junit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Logs {@link System#out} to string that can be retrieve using
 * {@link SystemOutWrapper#getSystemOut()}.
 * 
 * Example: <code>
	SystemOutWrapper wrapper = new SystemOutWrapper() {
 * 
 * @Override public void run() throws Exception { parser.start(args); }
 *           }.start(); </code>
 */
// TODO: extend to support System.err
public abstract class SystemOutWrapper {

	private ByteArrayOutputStream out;

	/**
	 * {@link Deprecated}, because you should never call this method.
	 * 
	 * @throws Exception
	 */
	@Deprecated
	public abstract void run() throws Exception;

	public String getSystemOut() {
		return out.toString();
	}

	public SystemOutWrapper start() throws Exception {
		PrintStream realOut = System.out;
		try {
			out = new ByteArrayOutputStream();
			System.setOut(new PrintStream(out));
			run();
		} finally {
			System.setOut(realOut);
		}
		return this;
	}
}
