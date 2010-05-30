package j2DbParser.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class Guicer extends AbstractModule {
	private static Injector injector = Guice.createInjector(new Guicer());

	public static void setInjector(Injector injector) {
		Guicer.injector = injector;
	}

	public static <T> T getInstance(Class<T> c) {
		return injector.getInstance(c);
	}

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new LoggerTypeListener());
	}
}
