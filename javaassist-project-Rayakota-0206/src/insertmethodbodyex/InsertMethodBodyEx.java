package insertmethodbodyex;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;
import util.UtilStr;

public class InsertMethodBodyEx extends ClassLoader {
	static String WORK_DIR = System.getProperty("user.dir");
	static String SEP = File.separator;
	static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
	static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
	private ClassPool pool;

	static String _L_ = System.lineSeparator();
	private static final String PKG_NAME = "target" + ".";

	public static void main(String[] args) throws NotFoundException {
		{
			while (true) {
				UtilMenu.showMenuOptions();
				int option = UtilMenu.getOption();
				switch (option) {
				case 1:
					System.out.println("=============================================");
					System.out.print(
							"Enter a application class name a method name,a method parameter index separated by , eg ComponentApp,foo,1 or ServiceApp,bar,2 \n");
					String[] test = UtilMenu.getArguments();
					if (test.length != 3) {
						System.out.println("[WRN] Invalid input");
						System.out.println("=============================================");
						System.out.println("Enter two variables separated by, \n");

					} else {
						try {
							// ClassPool pool = ClassPool.getDefault();
							// pool.insertClassPath(INPUT_DIR);
							// CtClass cc = pool.get(PKG_NAME + test[0]);
							// CtMethod m = cc.getDeclaredMethod(test[1]);
							ClassPool defaultPool = ClassPool.getDefault();
							defaultPool.insertClassPath(INPUT_DIR);
							CtClass cc = defaultPool.get(PKG_NAME + test[0]);
							CtMethod m = cc.getDeclaredMethod(test[1]);
							m.useCflow(test[1]);

							String block1 = "{ " + _L_ //
									+ "System.out.println(\"[INSERTED] target." + test[0] + "." + test[1] + "'s param "
									+ test[2] + ": \" + $" + test[2] + "); " + _L_ //
									+ "}";
							// String block1 = "{ " + _L_ //
							// + "System.out.println(\"[DBG] param1: \" $" + param + "); " + _L_ //
							// + "}";
							System.out.println(block1);
							System.out.println("------------------------------------------");
							m.insertBefore(block1);
							cc.writeFile(OUTPUT_DIR);
							System.out.println("[DBG] write output to: " + OUTPUT_DIR);
							System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
							InsertMethodBodyEx s = new InsertMethodBodyEx();
							Class<?> c = s.loadClass(PKG_NAME + test[0]);
							Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
							mainMethod.invoke(null, new Object[] { args });

						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CannotCompileException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;
				default:
					break;
				}

			}
		}
	}

	public InsertMethodBodyEx() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(OUTPUT_DIR); // TARGET must be there.
		UtilStr.print("[CLASS-LOADER] CLASS_PATH: " + INPUT_DIR);
		System.out.println("------------------------------------------");
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CtClass cc = null;
		try {
			cc = pool.get(name);
			byte[] b = cc.toBytecode();
			return defineClass(name, b, 0, b.length);
		} catch (NotFoundException e) {
			throw new ClassNotFoundException();
		} catch (IOException e) {
			throw new ClassNotFoundException();
		} catch (CannotCompileException e) {
			throw new ClassNotFoundException();
		}
	}
}
