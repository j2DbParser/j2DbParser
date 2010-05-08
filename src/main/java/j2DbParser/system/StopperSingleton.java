package j2DbParser.system;

public class StopperSingleton {
	/**
	 * The unique instance of this class.
	 */
	private static StopperSingleton instance = new StopperSingleton();

	/**
	 * Prevent instances of this class from being created.
	 */
	protected StopperSingleton() {
	}

	/**
	 * Return the unique instance of this class.
	 * 
	 * @return the unique instance of this class
	 */
	public static StopperSingleton getInstance() {
		return instance;
	}

	/**
	 * For tests
	 * 
	 * @param instance
	 *            mock instance
	 */
	public static void setInstance(StopperSingleton instance) {
		System.out.println("StopperSingleton.setInstance(" + instance + ")");
		StopperSingleton.instance = instance;
	}

	public void stop(String msg) {
		System.err.println(msg);
		stop();
	}

	public void stop(Exception e) {
		stop(e.getMessage());
	}

	public void stop() {
		System.exit(0);
	}
}
