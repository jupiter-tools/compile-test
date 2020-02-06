package com.jupitertools.compiletest;



class Code {

	private String className;
	private String classCode;

	Code(String className, String classCode) {
		this.className = className;
		this.classCode = classCode;
	}

	String getClassName() {
		return className;
	}

	String getClassCode() {
		return classCode;
	}
}