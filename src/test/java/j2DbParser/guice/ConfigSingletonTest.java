package j2DbParser.guice;

import static org.junit.Assert.assertTrue;
import j2DbParser.ConfigSingleton;

import org.junit.Test;

import com.google.inject.Guice;


public class ConfigSingletonTest {

	
	@Test
	public void testSingleton() throws Exception {
		ConfigSingleton instance1 = Guicer.getInstance(ConfigSingleton.class);
		ConfigSingleton instance2 = Guicer.getInstance(ConfigSingleton.class);
		assertTrue(instance1==instance2);
		
		Guicer.setInjector(Guice.createInjector(new GuicerMock()));
		
		ConfigSingleton instance3 = Guicer.getInstance(ConfigSingleton.class);
		ConfigSingleton instance4 = Guicer.getInstance(ConfigSingleton.class);
		assertTrue(instance3==instance4);
		
		assertTrue(instance1!=instance3);
		assertTrue(instance2!=instance4);
		assertTrue(instance1==instance2);
		
	}
}
