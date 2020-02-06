package com.jupitertools.compiletest;

import java.io.IOException;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;



class StringJavaFileObject extends SimpleJavaFileObject {

	private final String code;

	public StringJavaFileObject(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
		      Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
		return code;
	}
}