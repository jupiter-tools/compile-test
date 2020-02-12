package com.jupitertools.compiletest;

import java.lang.reflect.InvocationTargetException;


/**
 * Provide an API to work with loaded objects from {@link CompileTest}.
 * You can call static methods on the loaded class through this API.
 * Used as part of {@link CompileTest} DSL to test a code compilation.
 *
 * @author Anatoliy Korovin
 */
public class LoadedClass {

	private final Class<?> classObject;

	LoadedClass(Class<?> classObject) {
		this.classObject = classObject;
	}

	/**
	 * Invoke a static method from a loaded class and return the result of invocation.
	 *
	 * @param methodName the name of the calling method
	 * @return the result of invocation
	 */
	public Object invokeStatic(String methodName) {
		try {
			return classObject.getMethod(methodName, null).invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}