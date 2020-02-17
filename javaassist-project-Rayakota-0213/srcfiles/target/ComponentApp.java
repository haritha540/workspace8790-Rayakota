package target;

public class ComponentApp {
   int comX = 11;
   int comY=11;
   String comID= "ComponentApp";

   public void move(int dx,int dy, String IDdy) {
      setX(dx);
      setY(dy);
      setIDY(IDdy);
   }

   int setX(int dx) {
      this.comX = dx;
      return comX;
   }
   int setY(int dy) {
	      this.comY = dy;
	      return comY;
	   }

   String setIDY(String IDdy) {
      this.comID = IDdy;
      return comID;
   }

   public static void main(String[] args) throws Exception {
      System.out.println("[ComponentApp] Run...");
      ComponentApp a = new ComponentApp();
      a.move(0,0,null);
      System.out.println("[ComponentApp] Done. ");
   }
}