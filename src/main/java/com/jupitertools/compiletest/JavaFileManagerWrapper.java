package com.jupitertools.compiletest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;


class JavaFileManagerWrapper extends ForwardingJavaFileManager {

	private final List<ClassJavaFileObject> generatedFiles;

	JavaFileManagerWrapper(JavaFileManager fileManager) {
		super(fileManager);
		generatedFiles = new ArrayList<>();
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
	                                           String className,
	                                           JavaFileObject.Kind kind,
	                                           FileObject sibling) throws IOException {

		ClassJavaFileObject file = new ClassJavaFileObject(className, kind);
		generatedFiles.add(file);
		return file;
	}

	List<ClassJavaFileObject> getGeneratedFiles() {
		return generatedFiles;
	}
}