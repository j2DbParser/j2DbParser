package j2DbParser.guice;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

class LoggerTypeListener implements TypeListener {
	@Override
	public <T> void hear(TypeLiteral<T> typeLiteral,
			TypeEncounter<T> typeEncounter) {
		for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
			if (field.getType() == Logger.class
					&& field.isAnnotationPresent(Inject.class)) {
				typeEncounter.register(new LoggerMembersInjector<T>(field));
			}
		}
	}
}