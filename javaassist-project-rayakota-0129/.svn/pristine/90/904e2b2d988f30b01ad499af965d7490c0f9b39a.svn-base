package ex01;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;

import util.UtilOption;

public class ToClass {
	private static final String PKG_NAME = "target" + ".";

	public static void main(String[] args) {
		while (true) {
			UtilOption.showMenuOptions();
			switch (UtilOption.getOption()) {
			case 1:
				System.out.println("Enter a class name (e.g., CommonServiceA or CommonComponentB");
				String[] inputs = UtilOption.getArguments();
				if (inputs.length == 1) {

					process(inputs[0], "id", "year");
				} else {
					System.out.println("[WRN] Invalid input");
					System.out.println("=============================================");
					System.out.println("Enter one class name only \n");
				}
				break;
			default:
				break;
			}
		}
	}

	static void process(String clazz, String field1, String field2) {
		try {
			ClassPool cp = ClassPool.getDefault();
			CtClass cc = cp.get(PKG_NAME + clazz);

			CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
			String block1 = "{ " //
					+ "System.out.println(\"[TR] " + field1 + ": \" + " + field1 + "); }";
			System.out.println("[DBG] BLOCK1: " + block1);
			declaredConstructor.insertAfter(block1);

			String block2 = "{ " //
					+ "System.out.println(\"[TR] " + field2 + ": \" + " + field2 + "); }";
			System.out.println("[DBG] BLOCK2: " + block2);
			declaredConstructor.insertAfter(block2);

			Class<?> c = cc.toClass();
			c.newInstance();

		} catch (NotFoundException | CannotCompileException e) {
			System.out.println(e.toString());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
