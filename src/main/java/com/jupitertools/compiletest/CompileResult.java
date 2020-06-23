package com.jupitertools.compiletest;

import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Provide an API to work with the result of a compilation.
 * Used as part of {@link CompileTest} DSL to test a code compilation.
 *
 * @author Anatoliy Korovin
 */
public class CompileResult {

	private final ClassLoader classLoader;
	private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

	CompileResult(ClassLoader classLoader,
	              List<Diagnostic<? extends JavaFileObject>> diagnostics) {
		this.classLoader = classLoader;
		this.diagnostics = diagnostics;
	}

	/**
	 * Class and create an instance of class by the name.
	 *
	 * @param className the name of necessary class
	 * @return new instance of loaded class wrapped into the {@link InstantiatedClass}
	 */
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

	/**
	 * Load class by the className without creating new instances
	 *
	 * @param className the name of class to load
	 * @return loaded class wrapped into the {@link LoadedClass}
	 */
	public LoadedClass loadClass(String className) {
		try {
			return new LoadedClass(classLoader.loadClass(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
		return diagnostics;
	}
}