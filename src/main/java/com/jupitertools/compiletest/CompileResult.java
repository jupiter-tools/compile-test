package com.jupitertools.compiletest;

public class CompileResult {

	private final ClassLoader classLoader;

	CompileResult(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public InstantiatedClass createClass(String className) {
		try {
			Class<?> loadClass = classLoader.loadClass(className);
			Object instance = loadClass.newInstance();
			return new InstantiatedClass(loadClass, instance);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public LoadedClass loadClass(String name) {
		try {
			return new LoadedClass(classLoader.loadClass(name));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}