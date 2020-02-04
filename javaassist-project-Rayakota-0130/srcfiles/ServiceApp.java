public class ServiceApp {
	 public static void main(String[] args) throws Exception {
	     System.out.println("Run in ServiceApp...");
	      ServiceApp localApp = new ServiceApp();
	      localApp.runService();
	      System.out.println("1) getClass() is used to show the name: " + localApp.getClass().getField("f2").getName());
	      System.out.println("2) .class is used to show the name: " + ServiceApp.class.getField("f2").getName());
	      System.out.println("3) Class.forName() is used to show the name: " + Class.forName(args[0]).getField(args[1]).getName());
	      System.out.println("4) Show the value :" + Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance()));
	      System.out.println("Done");
}

private void runService() {
	// TODO Auto-generated method stub
	System.out.println("Called runService");
}
}
