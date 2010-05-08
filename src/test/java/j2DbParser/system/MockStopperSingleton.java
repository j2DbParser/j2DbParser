package j2DbParser.system;


public class MockStopperSingleton extends StopperSingleton {
	public static void init() {
		StopperSingleton.setInstance(new MockStopperSingleton());
	}

	@Override
	public void stop() {
		throw new IllegalStateException("stop");
	}

	@Override
	public void stop(String msg) {
		throw new IllegalStateException(msg);
	}

	@Override
	public void stop(Exception e) {
		throw new IllegalStateException(e.getMessage());
	}
}
