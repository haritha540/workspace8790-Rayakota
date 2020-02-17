package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
	static final String WORK_DIR = System.getProperty("user.dir");
	static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
	static String TARGET_MY_APP2;
	static int count;
	static String _L_ = System.lineSeparator();

	public static void main(String[] args) throws Throwable {
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("=============================================");
				System.out.print(
						"Enter a application class name ,number of member fields to be analyzed and displayed separated by , eg target.ComponentApp,1 or target.ServiceApp,100 \n");
				String[] test = UtilMenu.getArguments();

				if (test.length != 2) {
					System.out.println("[WRN] Invalid input");
					System.out.println("=============================================");
					System.out.println("Enter  two values separated by, \n");
					UtilMenu.showMenuOptions();
				} else {
					TARGET_MY_APP2 = test[0];
					count = Integer.parseInt(test[1]);
					NewExprAccess s = new NewExprAccess();
					Class<?> c = s.loadClass(TARGET_MY_APP2);
					Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
					mainMethod.invoke(null, new Object[] { args });
				}
			}
		}

	}

	private ClassPool pool;

	public NewExprAccess() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
		pool.insertClassPath(CLASS_PATH); // TARGET must be there.
	}

	/*
	 * Finds a specified class. The bytecode for that class can be modified.
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			cc.instrument(new ExprEditor() {
				@Override
				public void edit(NewExpr newExpr) throws CannotCompileException {
					try {
						String longName = newExpr.getConstructor().getLongName();
						if (longName.startsWith("java.")) {
							return;
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
					String log = String.format("[Edited by ClassLoader] new expr: %s, " //
							+ "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
							newExpr.getLineNumber(), newExpr.getSignature());
					System.out.println(log);
					CtField[] fields = newExpr.getEnclosingClass().getDeclaredFields();
					int field_num = fields.length;
					if (field_num < count) {
						count = field_num;
					}
					String block1 = "{ " + _L_ //
							+ " $_ = $proceed($$);";
					// String[] p = { "srcX", "srcY", "srcID" };

					for (int i = 0; i < count; i++) {

						try {
							String fieldName = fields[i].getName();
							String fieldType = fields[i].getType().getName();

							block1 = block1 + "{" + _L_//
									+ " String cName =$_.getClass().getName();" + _L_ //
									+ " String fName=$_.getClass().getDeclaredFields()[" + i + "].getName();" + _L_//
									+ " String fieldFullName = cName + \".\" + fName;" + _L_ //
									+ fieldType + " fieldvalue = $_." + fieldName + ";" + _L_ //
									+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldvalue);"
									+ _L_ //

									+ "}" + _L_//
							;
							// + " System.out.println($type);" + _L_

						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					block1 = block1 + "}";

					System.out.println(block1);
					System.out.println("----------------------------");
					newExpr.replace(block1);
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