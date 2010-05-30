package j2DbParser.guice;

import j2DbParser.system.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import com.google.inject.MembersInjector;

class LoggerMembersInjector<T> implements MembersInjector<T> {
	private final Field field;
	private final Logger logger;

	LoggerMembersInjector(Field field) {
		this.field = field;
		this.logger = LogFactory.getLogger(field.getDeclaringClass());
		field.setAccessible(true);
	}

	public void injectMembers(T t) {
		try {
			field.set(t, logger);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}