package j2DbParser.junit;

import java.lang.reflect.Method;

import org.easymock.classextension.EasyMock;

/**
 * Partial mock support for EasyMock
 */
public final class PartialMockFactory {

	private PartialMockFactory() {
	}

	public static <T> T createMock(Class<T> clazz, String... methodsToMock)
			throws SecurityException {
		return EasyMock.createNiceMock(clazz, find(clazz, methodsToMock));
	}

	public static <I, T extends I> T createMock(Class<T> clazz,
			Class<I> interfaceWithMethodsToMock) {
		Method[] methodsFromInterface = interfaceWithMethodsToMock.getMethods();
		Method[] outMethods = new Method[methodsFromInterface.length];
		try {
			for (int i = 0; i < methodsFromInterface.length; i++) {
				Method inMethod = methodsFromInterface[i];
				Method outMethod;
				outMethod = clazz.getMethod(inMethod.getName(), inMethod
						.getParameterTypes());
				outMethods[i] = outMethod;
			}
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		return EasyMock.createNiceMock(clazz, outMethods);
	}

	private static Method[] find(Class<?> clazz, String... methodsToFind)
			throws SecurityException {
		Method[] outMethods = new Method[methodsToFind.length];
		for (int i = 0; i < methodsToFind.length; i++) {
			String m = methodsToFind[i];
			outMethods[i] = findWithSubclasses(clazz, m);
		}
		return outMethods;
	}

	private static Method findWithSubclasses(Class<?> clazz, String methodToFind) {
		Method outMethod = null;

		while (clazz != null && !clazz.equals(Object.class)) {
			Method[] declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				if (methodToFind.equals(method.getName())) {
					outMethod = method;
					break;
				}
			}
			clazz = clazz.getSuperclass();
		}

		if (outMethod == null) {
			throw new RuntimeException(clazz + "doesn't have method "
					+ methodToFind);
		}
		return outMethod;
	}

}
