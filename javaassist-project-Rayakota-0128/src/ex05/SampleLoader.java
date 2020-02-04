package ex05;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class SampleLoader {

	static final String SEP = File.separator;
	static String workDir = System.getProperty("user.dir");
	static String outputDir = workDir + File.separator + "output";
	// static boolean hasmodified = false;
	static String subclass0;
	static String subclass1;
	static String superclass1;
	static List<String> argTest = new ArrayList<String>();
	static List<String> my_dict = new ArrayList<String>();
	static List<String> my_test = new ArrayList<String>();
	static ClassPool cp = ClassPool.getDefault();
	static CtClass cc;
	static CtClass cc1;

	public static void main(String[] args) {
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("=============================================");
				System.out.print("Enter three classes separated by , eg Point,Circle,Rectangle \n");
				String[] test = UtilMenu.getArguments();

				if (test.length != 3) {
					System.out.println("[WRN] Invalid input");
					System.out.println("=============================================");
					System.out.println("Enter three classes separated by, \n");

				} else {
					superclasssetting(test[0], test[1], test[2]);
					System.out.println(subclass0);
					System.out.println("[DBG] Enter (1)Usage Method" + "(2) a increment method "
							+ "(3) a getter method (eg:add,incX,getX or remove,incY,getY)");//
					String[] argument1 = UtilMenu.getArguments();
					try {
						insertClassPath(cp);
						cc = setSuperclass(cp, subclass0, superclass1);
						cc1 = setSuperclass(cp, subclass1, superclass1);
					} catch (Exception e) {
					}

					if (argument1[0].equals("add")) {

						if ((my_dict.contains(argument1[0])) && (argTest.contains(argument1[1]))) {
							System.out.println("[WRN] This method " + argument1[0] + " has already been modified");
							System.out.println("=============================================");
							System.out.println("Enter a different method name \n");
							UtilMenu.showMenuOptions();

						}

						else {

							try {
								addMethodCall(cp, cc, argument1[0], argument1[1], argument1[2]);
								executeMethodCall(cp, subclass0, argument1[0], argument1[2]);
								cc.writeFile(outputDir);
								System.out.println("[DBG] write output to: " + outputDir);
								addMethodCall(cp, cc1, argument1[0], argument1[1], argument1[2]);
								executeMethodCall(cp, subclass1, argument1[0], argument1[2]);
								cc1.writeFile(outputDir);
								System.out.println("[DBG] write output to: " + outputDir);
								my_dict.add(argument1[0]);
								argTest.add(argument1[1]);
							} catch (Exception e) {

							}
						}
					} else {
						if ((my_dict.contains(argument1[0])) && (my_test.contains(argument1[1]))) {
							System.out.println("[WRN] This method " + argument1[0] + " has already been modified");
							System.out.println("=============================================");
							System.out.println("Enter a different method name \n");
							UtilMenu.showMenuOptions();

						}

						else {

							try {
								addMethodCall(cp, cc, argument1[0], argument1[1], argument1[2]);
								executeMethodCall(cp, subclass0, argument1[0], argument1[2]);
								cc.writeFile(outputDir);
								System.out.println("[DBG] write output to: " + outputDir);
								addMethodCall(cp, cc1, argument1[0], argument1[1], argument1[2]);
								executeMethodCall(cp, subclass1, argument1[0], argument1[2]);
								cc1.writeFile(outputDir);
								System.out.println("[DBG] write output to: " + outputDir);
								my_dict.add(argument1[0]);
								my_test.add(argument1[1]);
							} catch (Exception e) {

							}
						}
					}
				}
				break;
			default:
				break;

			}
		}
	}

	static void addMethodCall(ClassPool cp, CtClass cc, String usagemethod, String incmethod, String getmethod)
			throws Exception {
		CtMethod m1 = cc.getDeclaredMethod(usagemethod);
		String BLK = "\n{\n" + incmethod + "();" + "\n" //
				+ "System.out.println(\"[TR] " + getmethod + "() " + "result : \" + " + getmethod + "() ); " + "}";
		System.out.println("[DBG] Block: " + BLK);
		cc.defrost();
		m1.insertBefore(BLK);
		// System.out.println("In here");

	}

	static void executeMethodCall(ClassPool cp, String subClass, String usagemethod, String getmethod)
			throws Exception {
		Loader cl = new Loader(cp);
		Class<?> c = cl.loadClass(subClass);
		Object rect = c.newInstance();
		System.out.println("[DBG] Created a  " + subClass + " object.");
		Class<?> rectClass = rect.getClass();
		Method m = rectClass.getDeclaredMethod(usagemethod, new Class[] {});
		System.out.println("[DBG] Called getDeclaredMethod.");
		Object retVal = m.invoke(c.newInstance(), new Object[] {});
		System.out.println("[DBG]" + getmethod + " result: " + retVal);
		System.out.println("In execute block");
	}

	static CtClass setSuperclass(ClassPool cp, String subClass, String superClass) throws Exception {
		CtClass cc = cp.get(subClass);
		cc.setSuperclass(cp.get(superClass));
		return cc;
	}

	static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + File.separator + "classfiles";
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}

	private static void superclasssetting(String test0, String test1, String test2) {
		String con = "target.";

		String firstArg = con.concat(test0);
		String secondArg = con.concat(test1);
		String thirdArg = con.concat(test2);

		String superClass;
		List<String> subClasses = new ArrayList<String>();
		int maxClassNameLength = 0;
		String maxClassNameLengthClass = "";

		subClasses.add(firstArg);
		subClasses.add(secondArg);
		subClasses.add(thirdArg);
		for (String className : subClasses) {
			if (className.startsWith("Common")) {
				if (className.length() > maxClassNameLength) {
					maxClassNameLength = className.length();
					maxClassNameLengthClass = className;
				}
			}
		}
		if (maxClassNameLength == 0) {
			superClass = firstArg;
		} else {
			superClass = maxClassNameLengthClass;
		}
		subClasses.remove(superClass);
		System.out.println("superClass:" + superClass);
		System.out.println("subClasses:" + subClasses);
		subclass0 = subClasses.get(0);
		subclass1 = subClasses.get(1);
		superclass1 = superClass;

	}
}