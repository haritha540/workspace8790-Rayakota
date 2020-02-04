package ex06;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class TestSampleLoader extends ClassLoader {

	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	static String INPUT_DIR = workDir + File.separator + "classfiles";
	static String TARGET_APP;
	static String TARGET_FIELD;
	private ClassPool pool;

	public static void main(String[] args) throws NotFoundException {
		{
			while (true) {
				UtilMenu.showMenuOptions();
				int option = UtilMenu.getOption();
				switch (option) {
				case 1:
					System.out.println("=============================================");
					System.out.print(
							"Enter a class name and field name separated by , eg ComponentApp,f1 or ServiceApp,f2 \n");
					String[] test = UtilMenu.getArguments();
					if (test.length != 2) {
						System.out.println("[WRN] Invalid input");
						System.out.println("=============================================");
						System.out.println("Enter two variables separated by, \n");

					} else {
						TARGET_APP = test[0];
						TARGET_FIELD = test[1];
						TestSampleLoader loader = new TestSampleLoader();
						try {
							Class<?> c = loader.findClass(TARGET_APP);
							c.getDeclaredMethod("main", new Class[] { String[].class }). //
									invoke(null, new Object[] { args });
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
								| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
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

	public TestSampleLoader() throws NotFoundException {
		pool = new ClassPool();
		pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			CtClass cc = pool.get(name);
			if (name.equals(TARGET_APP)) {
				CtField f = new CtField(CtClass.doubleType, TARGET_FIELD, cc);
				f.setModifiers(Modifier.PUBLIC);
				cc.addField(f);
			}
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