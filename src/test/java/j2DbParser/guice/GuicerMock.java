package j2DbParser.guice;

import j2DbParser.ConfigSingleton;

import java.util.logging.Logger;

import com.google.inject.Provider;
import com.google.inject.Singleton;

public class GuicerMock extends Guicer {
	
	public static class ConfigSingletonMock extends ConfigSingleton {

	}

	@Override
	protected void configure() {
		System.out.println("GuicerMock.configure()");
		bind(ConfigSingleton.class).to(ConfigSingletonMock.class).in(
				Singleton.class);
		if (false) {
			bind(Logger.class).toProvider(new Provider<Logger>() {
				@Override
				public Logger get() {
					System.out.println("get()");
					return Logger.getLogger("bla");
				}
			});
		}
		requestStaticInjection(GuiceLoggerFunTest.class);
	}

}
