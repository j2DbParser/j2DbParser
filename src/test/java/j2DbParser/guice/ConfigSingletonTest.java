package j2DbParser.guice;

import static org.junit.Assert.assertTrue;
import j2DbParser.ConfigSingleton;

import org.junit.Test;

import com.google.inject.Guice;


public class ConfigSingletonTest {

	
	@Test
	public void testSingleton() throws Exception {
		ConfigSingleton instance1 = GuiceInjector.getInstance(ConfigSingleton.class);
		ConfigSingleton instance2 = GuiceInjector.getInstance(ConfigSingleton.class);
		assertTrue(instance1==instance2);
		
		GuiceInjector.setInjector(Guice.createInjector(new GuicerMock()));
		
		ConfigSingleton instance3 = GuiceInjector.getInstance(ConfigSingleton.class);
		ConfigSingleton instance4 = GuiceInjector.getInstance(ConfigSingleton.class);
		assertTrue(instance3==instance4);
		
		assertTrue(instance1!=instance3);
		assertTrue(instance2!=instance4);
		assertTrue(instance1==instance2);
		
	}
}
