package com.jupitertools.compiletest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CompileTestTest {

	@Nested
	class SimpleTests {

		@Test
		void newInstance() {
			Object result = new CompileTest().classCode("A",
			                                            "" +
			                                            "public class A { " +
			                                            "  public String tst(){" +
			                                            "      return A.class.toString();" +
			                                            "  } " +
			                                            "}")
			                                 .compile()
			                                 .createClass("A")
			                                 .invoke("tst");
			System.out.println(result);
			assertThat(result).isEqualTo("class A");
		}

		@Test
		void invokeStatic() {
			Object result = new CompileTest().classCode("A",
			                                            "" +
			                                            "public class A { " +
			                                            "  public static String tst(){" +
			                                            "      return A.class.toString();" +
			                                            "  } " +
			                                            "}")
			                                 .compile()
			                                 .loadClass("A")
			                                 .invokeStatic("tst");
			System.out.println(result);
			assertThat(result).isEqualTo("class A");
		}
	}

	@Nested
	class CompileMultipleClasses {

		@Test
		void newInstance() {
			String result = (String) new CompileTest().classCode("C", "public class C {" +
			                                                          "    public String toString(){" +
			                                                          "        return \"C:ABCD:\";" +
			                                                          "    }" +
			                                                          "}")
			                                          .classCode("D", "public class D {}")
			                                          .classCode("HelloWorld",
			                                                     "" +
			                                                     "public class HelloWorld {" +
			                                                     "   public String tst(){" +
			                                                     "       int x = 51187;  " +
			                                                     "       C c = new C();" +
			                                                     "       return  c.toString() + x;" +
			                                                     "   }" +
			                                                     "}")
			                                          .compile()
			                                          .createClass("HelloWorld")
			                                          .invoke("tst");
			System.out.println(result);
			assertThat(result).isEqualTo("C:ABCD:51187");
		}

		@Test
		void invokeStatic() {
			// Assert
			Object result = new CompileTest().classCode("C", "public class C { " +
			                                                 " public static String tst(){ return C.class.toString(); } " +
			                                                 "}")
			                                 .classCode("D", "public class D {}")
			                                 .classCode("HelloWorld",
			                                            "" +
			                                            "public class HelloWorld { " +
			                                            "  public static String test(){ " +
			                                            "    return \"Hey-Ho! \"+C.tst();" +
			                                            "  }" +
			                                            "}")
			                                 .compile()
			                                 .loadClass("HelloWorld")
			                                 //Act
			                                 .invokeStatic("test");
			// Assert
			System.out.println(result);
			assertThat(result).isEqualTo("Hey-Ho! class C");
		}
	}

	@Nested
	class ProcessorsInherit {

		@Test
		void lombokTest() {

			String code = "import lombok.Getter;" +
			              " " +
			              "public class A {" +
			              "  @Getter" +
			              "  public String tst = \"ABC\";" +
			              "}";

			Object result = new CompileTest().classCode("A", code)
			                                 .inheritAllProcessors()
			                                 .compile()
			                                 .createClass("A")
			                                 .invoke("getTst");

			assertThat(result).isEqualTo("ABC");
		}

		@Test
		void error() {
			String code = "import lombok.Getter;" +
			              "" +
			              "public class A {" +
			              "  @Getter" +
			              "  public String tst = \"ABC\";" +
			              "}";

			InstantiatedClass instance = new CompileTest().classCode("A", code)
			                                              .compile()
			                                              .createClass("A");

			RuntimeException exc = Assertions.assertThrows(RuntimeException.class,
			                                               () -> instance.invoke("getTst"));

			assertThat(exc.getCause()).isInstanceOf(NoSuchMethodException.class);
			assertThat(exc.getMessage()).contains("A.getTst()");
		}
	}

	@Nested
	class CompilationOptions {

		@Test
		void options() {
			CompileResult result = new CompileTest().classCode("A",
			                                                   "public class A { " +
			                                                   "  public String tst(){" +
			                                                   // Warning about unnecessary class cast:
			                                                   "           return (String)\"123\";" +
			                                                   "  } " +
			                                                   "}")
			                                        // any warning will fail the compilation process:
			                                        .options("-Xlint:cast", "-Werror")
			                                        .compile();

			Assertions.assertThrows(Exception.class, () -> result.loadClass("A"));
		}

		@Test
		void diagnostics() {
			CompileResult result = new CompileTest().classCode("A",
			                                                   "public class A { " +
			                                                   "  public String tst(){" +
			                                                   // Warning about unnecessary class cast:
			                                                   "           return (String)\"123\";" +
			                                                   "  } " +
			                                                   "}")
			                                        // any warning will fail the compilation process:
			                                        .options("-Xlint:cast")
			                                        .compile();

			System.out.println(result.getDiagnostics());
		}
	}

}