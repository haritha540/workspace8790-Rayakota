package ex02.definenewclass;

import java.io.File; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import util.UtilOption;

public class DefineNewClass {
	static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + File.separator + "output";

   public static void main(String[] args) {
	     File directory=new File(String.valueOf(outputDir));
         if(!directory.exists()) {
        	 directory.mkdir();
        	 System.out.println("sucess");
         }
         UtilOption.showMenuOptions();
  	   UtilOption.getOption();
  	   System.out.println("=============================================");
         System.out.print("Enter three classes separated by ,\n");
  	   String[] test=UtilOption.getArguments();
  	   if(test.length!=3) {
        	 System.out.println("[WRN] Invalid input");
             System.out.println("=============================================");
             System.out.println("Enter three classes separated by, \n");
        	 
         }


	    
	         
	        
      try {
         ClassPool pool = ClassPool.getDefault();
         String firstArg = test[0];
			String secondArg = test[1];
			String thirdArg = test[2];
		     CtClass cc = makeClass(pool, test[0]);
	         cc.writeFile(outputDir);
	         System.out.println("[DBG] write output to: " + outputDir);
	         CtClass cc1 = makeClass(pool, test[1]);
	         cc1.writeFile(outputDir);
	         System.out.println("[DBG] write output to: " + outputDir);
	         

	         CtClass cc2 = makeClass(pool, test[2]);
	         cc2.writeFile(outputDir);
	         System.out.println("[DBG] write output to: " + outputDir);
			if (test.length != 3) {
				System.out.println("Terminate the program as arguments are less than required");
			}
			
        String superClass;
		List<String> subClasses = new ArrayList<String>();
		int maxClassNameLength = 0;
		String maxClassNameLengthClass = "";
		//System.out.println(firstArg);
		subClasses.add(firstArg);
		subClasses.add(secondArg);
		subClasses.add(thirdArg);
		
		for (String className : test) {
			if (className.startsWith("Common")) {
				if (className.length() > maxClassNameLength) {
					maxClassNameLength = className.length();
					maxClassNameLengthClass = className;
				}
			}
		}
		if (maxClassNameLength == 0) {
			superClass = test[0];
		} else {
			superClass = maxClassNameLengthClass;
		}
		subClasses.remove(superClass);
		System.out.println("superClass:" + superClass);
		System.out.println("subClasses:" + subClasses);
		insertClassPath(pool);
		for (String subClass : subClasses) {
			CtClass cc3 = pool.get(subClass);
					 cc3.defrost(); // modifications of the class definition will be permitted.
	       			setSuperclass(cc3,superClass, pool);
	       			System.out.println(subClass);
			cc3.writeFile(outputDir);
		}

		System.out.println("[DBG] write output to: " + outputDir);


	

      
      
      }    
      
      
      
      
      catch (CannotCompileException | IOException e) {
         e.printStackTrace();
      } catch (NotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
   }

   static CtClass makeClass(ClassPool pool, String newClassName) {
      CtClass cc = pool.makeClass(newClassName);
      System.out.println("[DBG] make class: " + cc.getName());
      return cc;
   }

   private static CtClass makeInterface(ClassPool pool, String newInterfaceName) {
      CtClass cc = pool.makeInterface(newInterfaceName);
      System.out.println("[DBG] make interface: " + cc.getName());
      return cc;
   }

static void insertClassPath(ClassPool pool) throws NotFoundException {
		String strClassPath = workDir + SEP + "bin"; // eclipse compile dir
		// String strClassPath = workDir + SEP + "classfiles"; // separate dir
		pool.insertClassPath(strClassPath);
		System.out.println("[DBG] insert classpath: " + strClassPath);
	}

	static void setSuperclass(CtClass curClass, String superClass, ClassPool pool)
			throws NotFoundException, CannotCompileException {
		curClass.setSuperclass(pool.get(superClass));
		System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
				", subclass: " + curClass.getName());
	}
}
	   