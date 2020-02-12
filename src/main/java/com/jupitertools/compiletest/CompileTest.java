package com.jupitertools.compiletest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.intellij.lang.annotations.Language;

/**
 * The main entry point in the CompileTest API.
 * <p>
 * You can use this class like a specific DSL to test compilation
 * of classes and annotation processing.
 *
 * @author Anatoliy Korovin
 */
public class CompileTest {

	private List<Code> classCodes = new ArrayList<>();
	private List<Processor> processors = new ArrayList<>();
	private boolean inheritAllProcessors = false;

	/**
	 * Add a code of class to compile
	 *
	 * @param name name of the class
	 * @param code code of the class
	 * @return created or modified {@link CompileTest} instance
	 */
	public CompileTest classCode(String name, @Language("Java") String code) {
		classCodes.add(new Code(name, code));
		return this;
	}

	/**
	 * Add a specified annotation processor to compile
	 *
	 * @param processor instance of the necessary annotation processor
	 * @return created or modified {@link CompileTest} instance
	 */
	public CompileTest processor(Processor processor) {
		processors.add(processor);
		return this;
	}

	/**
	 * Terminal action, compiles all previously added classes with
	 * selected annotation processors
	 *
	 * @return the result of compilation {@link CompileResult}
	 */
	public CompileResult compile() {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		List<JavaFileObject> compilationUnits = classCodes.stream()
		                                                  .map(code -> new StringJavaFileObject(code.getClassName(),
		                                                                                        code.getClassCode()))
		                                                  .collect(Collectors.toList());

		JavaFileManagerWrapper fileManager =
				new JavaFileManagerWrapper(compiler.getStandardFileManager(null,
				                                                           null,
				                                                           null));

		JavaCompiler.CompilationTask compilationTask = compiler.getTask(null,
		                                                                fileManager,
		                                                                null,
		                                                                null,
		                                                                null,
		                                                                compilationUnits);
		if (!inheritAllProcessors) {
			compilationTask.setProcessors(processors);
		}
		compilationTask.call();
		CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedFiles());
		return new CompileResult(classLoader);
	}

	/**
	 * Use all annotation processors from the place of calling
	 *
	 * @return modified {@link CompileTest} instance
	 */
	public CompileTest inheritAllProcessors() {
		this.inheritAllProcessors = true;
		return this;
	}
}