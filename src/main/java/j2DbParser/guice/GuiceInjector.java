package j2DbParser.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;

public class GuiceInjector extends AbstractModule {
	private static Injector injector = Guice.createInjector(new GuiceInjector());

	public static void setInjector(Injector injector) {
		GuiceInjector.injector = injector;
	}

	public static <T> T getInstance(Class<T> c) {
		return injector.getInstance(c);
	}

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new LoggerTypeListener());
	}
}
