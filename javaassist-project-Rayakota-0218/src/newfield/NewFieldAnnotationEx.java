package newfield;

import java.io.File;
import java.lang.reflect.Proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import target.Author;
import util.UtilMenu;

public class NewFieldAnnotationEx {
	static String workDir = System.getProperty("user.dir");
	static String inputDir = workDir + File.separator + "classfiles";
	static String outputDir = workDir + File.separator + "output";

	public static void main(String[] args) {
		while (true) {
			UtilMenu.showMenuOptions();
			int option = UtilMenu.getOption();
			switch (option) {
			case 1:
				System.out.println("=============================================");
				System.out.print(
						"Enter a application class name ,name of the first annotation,name of the second annotation separated by , eg ComponentApp,Column,Author or ServiceApp,Row,Author \n");
				String[] test = UtilMenu.getArguments();

				if (test.length != 3) {
					System.out.println("[WRN] Invalid input");
					System.out.println("=============================================");
					System.out.println("Enter  three values separated by, \n");
					UtilMenu.showMenuOptions();
				} else {
					ClassPool pool = ClassPool.getDefault();
					try {
						pool.insertClassPath(inputDir);

						CtClass ct = pool.get("target." + test[0]);
						// process(ct.getAnnotations());
						CtField[] cf = ct.getDeclaredFields();
						// CtField cf1 = ct.getField("componentProvider");
						for (int i = 0; i < cf.length; i++) {
							process(cf[i].getAnnotations(), test[1]);
						}
					} catch (NotFoundException | ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}

	static void process(Object[] annoList, String test1) {

		String Target_c = "target." + test1;
		for (int i = 0; i < annoList.length; i++) {
			// System.out.println(annoList[i]);
			Annotation annotation = getAnnotation(annoList[i]);
			if (annotation.getTypeName().equals(Target_c)) {
				for (int j = 0; j < annoList.length; j++) {
					// .out.println(annoList[j]);
					if (annoList[j] instanceof Author) {
						Author author = (Author) annoList[j];
						System.out.println("Name: " + author.name() + ", Year: " + author.year());
					}
				}
			}
		}
	}

	static Annotation getAnnotation(Object obj) {
		// Get the underlying type of a proxy object in java
		AnnotationImpl annotationImpl = //
				(AnnotationImpl) Proxy.getInvocationHandler(obj);
		return annotationImpl.getAnnotation();
	}
}
