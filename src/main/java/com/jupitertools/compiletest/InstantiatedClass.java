package com.jupitertools.compiletest;

import java.lang.reflect.InvocationTargetException;


/**
 * Provide an API to work with instantiated objects from {@link CompileTest}.
 * You can call methods on a current instance of the created object through this API.
 * Used as part of {@link CompileTest} DSL to test a code compilation.
 *
 * @author Anatoliy Korovin
 */
public class InstantiatedClass {

	private final Class<?> classObject;
	private final Object instance;

	InstantiatedClass(Class<?> classObject, Object instance) {
		this.classObject = classObject;
		this.instance = instance;
	}

	/**
	 * Call the method by the name on the {@link InstantiatedClass} instance.
	 *
	 * @param methodName the name of calling method
	 * @return the result of invocation
	 */
	public Object invoke(String methodName) {
		try {
			return classObject.getMethod(methodName, null).invoke(instance);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}