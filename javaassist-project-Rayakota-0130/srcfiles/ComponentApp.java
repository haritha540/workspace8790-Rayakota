
public class ComponentApp {
	 public static void main(String[] args) throws Exception {
	     System.out.println("Run in Component App...");
	      ComponentApp localMyApp = new ComponentApp();
	      localMyApp.runComponent();
	      System.out.println("1) getClass() is used to show the name: " + localMyApp.getClass().getField("f1").getName());
	      System.out.println("2) .class is used to show the name: " + ComponentApp.class.getField("f1").getName());
	      System.out.println("3) Class.forName() is used to show the name: " + Class.forName(args[0]).getField(args[1]).getName());
	     // System.out.println("4) Class.forName() is used to show the name: " + Class.forName(args[0]).getField(args[1]).get(Class.forName(arg[0]).newInstance());
	      System.out.println("4) Show the value: " + Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance()));
	      System.out.println("Done");
}

private void runComponent() {
	// TODO Auto-generated method stub
	System.out.println("Called runComponent");
}
}


