package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

	static String TARGET_MY_APP;
	static String MOVE_METHOD;
	static final String DRAW_METHOD = "draw";
	static String METHOD_ARG;
	static String METHOD_VALUE;
	static String _L_ = System.lineSeparator();
	private static final String PKG_NAME = "target" + ".";
	static List<String> my_test = new ArrayList<String>();
	static List<String> my_dict = new ArrayList<String>();

	public static void main(String[] args) throws Throwable {

		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("=============================================");
				System.out.print(
						"Enter a application class name ,a method name,a method parameter index,a value to be modified separated by , eg ComponentApp,move,1,0 or ServiceApp,fill,2,10 \n");
				String[] test = UtilMenu.getArguments();

				if (test.length != 4) {
					System.out.println("[WRN] Invalid input");
					System.out.println("=============================================");
					System.out.println("Enter four variables separated by, \n");

				} else {
					TARGET_MY_APP = PKG_NAME + test[0];
					MOVE_METHOD = test[1];
					METHOD_ARG = test[2];
					METHOD_VALUE = test[3];
					if (my_test.contains(MOVE_METHOD)) {
						System.out.println("[WRN] This method " + MOVE_METHOD + " has already been modified");
						System.out.println("=============================================");
						System.out.println("Enter a different method name \n");
						UtilMenu.showMenuOptions();
					} else {
						SubstituteMethodBody s = new SubstituteMethodBody();
						Class<?> c = s.loadClass(TARGET_MY_APP);
						Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
						mainMethod.invoke(null, new Object[] { args });
					}
				}
			}

		}

	}

	private ClassPool pool;

	public SubstituteMethodBody() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(INPUT_PATH); // "target" must be there.
		System.out.println("[DBG] Class Pathes: " + pool.toString());
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			if (!cc.getName().equals(TARGET_MY_APP)) {
				return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
			}

			cc.instrument(new ExprEditor() {
				@Override
				public void edit(MethodCall call) throws CannotCompileException {
					String className = call.getClassName();
					String methodName = call.getMethodName();

					if (className.equals(TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
						System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: "
								+ call.getLineNumber());
						String block1 = "{" + _L_ //
								+ "System.out.println(\"Before a call to " + methodName + ".\"); " + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "System.out.println(\"After a call to " + methodName + ".\"); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK1: " + block1);
						System.out.println("------------------------");
						call.replace(block1);
					} else if (className.equals(TARGET_MY_APP) && methodName.equals(MOVE_METHOD)) {

						System.out.println("[Edited by ClassLoader] method name: " + methodName + ", line: "
								+ call.getLineNumber());
						String block2 = "{" + _L_ //
								+ "System.out.println(\"\tReset param " + METHOD_ARG + " to " + METHOD_VALUE
								+ ".\t\"); " + _L_ //
								+ "$" + METHOD_ARG + "=" + METHOD_VALUE + ";" + _L_ //
								+ "$proceed($$); " + _L_ //
								+ "}";
						System.out.println("[DBG] BLOCK2: " + block2);
						System.out.println("------------------------");
						call.replace(block2);
						my_test.add(MOVE_METHOD);

					}
				}
			});
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			e.printStackTrace();
			throw new ClassNotFoundException();
		}
	}
}
