package target;

public class ServiceApp {
   int x = 10, y = 10;

   public void draw() {
      fill(2, 2);
   }

   public void fill(int dx, int dy) {
      x = dx;
      y = dy;
      System.out.println("\t[ServiceApp] x: " + x + ", y: " + y);
      System.out.println("\t[SerrviceApp] dx: " + dx + ", dy: " + dy);
   }

   public static void main(String[] args) throws Exception {
      System.out.println("[ServiceApp] Run...");
      ServiceApp a = new ServiceApp();
      a.draw();
      System.out.println("[ServiceApp] Done.");
   }
}
