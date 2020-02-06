package com.jupitertools.compiletest;

import java.lang.reflect.InvocationTargetException;

public class InstantiatedClass {

	private final Class<?> classObject;
	private final Object instance;

	InstantiatedClass(Class<?> classObject, Object instance) {
		this.classObject = classObject;
		this.instance = instance;
	}

	public Object invoke(String methodName) {
		try {
			return classObject.getMethod(methodName, null).invoke(instance);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}