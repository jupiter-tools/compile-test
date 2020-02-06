package com.jupitertools.compiletest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;


class ClassJavaFileObject extends SimpleJavaFileObject {

	private final ByteArrayOutputStream outputStream;
	private final String className;

	ClassJavaFileObject(String className, Kind kind) {
		super(URI.create("mem:///" + className.replace('.', '/') + kind.extension), kind);
		this.className = className;
		outputStream = new ByteArrayOutputStream();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return outputStream;
	}

	byte[] getBytes() {
		return outputStream.toByteArray();
	}

	String getClassName() {
		return className;
	}
}