import java.io.IOException;
import java.io.BufferedOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestSocket {

   public static void main(String[] args) {
      try {
         Socket sock = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(args[0]));
         String data = args[1];
         System.out.println(sock.toString());
         BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());
         String request = "Salut\n";
         bos.write(request.getBytes());
         bos.flush();
      } catch (UnknownHostException e) {
         e.printStackTrace();
      }catch (IOException e) {
         e.printStackTrace();
      }
   }
}
