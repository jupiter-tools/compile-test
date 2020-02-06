package com.jupitertools.compiletest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.intellij.lang.annotations.Language;



public class CompileTest {

	private List<Code> classCodes = new ArrayList<>();
	private List<Processor> processors = new ArrayList<>();

	public CompileTest classCode(String name, @Language("Java") String code) {
		classCodes.add(new Code(name, code));
		return this;
	}

	public CompileTest processor(Processor processor) {
		processors.add(processor);
		return this;
	}

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

		compilationTask.setProcessors(processors);
		compilationTask.call();
		CompiledClassLoader classLoader = new CompiledClassLoader(fileManager.getGeneratedFiles());
		return new CompileResult(classLoader);
	}
}