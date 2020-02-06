package com.jupitertools.compiletest;

import java.lang.reflect.InvocationTargetException;


public class LoadedClass {

	private final Class<?> classObject;

	LoadedClass(Class<?> classObject) {
		this.classObject = classObject;
	}

	public Object invokeStatic(String methodName) {
		try {
			return classObject.getMethod(methodName, null).invoke(null);
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}