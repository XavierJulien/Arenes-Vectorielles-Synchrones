import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestSocket {

   public static void main(String[] args) {
      try {
         Socket sock = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(args[0]));
         String name = args[1];
         PrintWriter out = new PrintWriter(sock.getOutputStream());
         out.println(name);
      } catch (UnknownHostException e) {
         e.printStackTrace();
      }catch (IOException e) {
         e.printStackTrace();
      }
   }
}
