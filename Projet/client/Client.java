import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.PrintStream;
// public class TestSocket {
//
//    public static void main(String[] args) {
//       try {
//          Socket sock = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt(args[0]));
//          String message = args[1];
//          PrintWriter out = new PrintWriter(sock.getOutputStream());
//
//          out.println(name);
//       } catch (UnknownHostException e) {
//          e.printStackTrace();
//       }catch (IOException e) {
//          e.printStackTrace();
//       }
//    }
// }


public class Client {
  protected static final int PORT=2019;

  public static void main(String[] args) {
    Socket s=null;
    if (args.length != 1) {
      System.err.println("Usage: java Client <hote>");
      System.exit(1); }
      try {
        s = new Socket (InetAddress.getByName("127.0.0.1"),PORT);
        DataInputStream canalLecture = new DataInputStream(s.getInputStream());
        PrintStream canalEcriture = new PrintStream(s.getOutputStream());
        System.out.println("Connexion etablie : "+
        s.getInetAddress()+" port : "+s.getPort());
        String ligne = new String();
        char c;
        while (true) {
          System.out.print("?"); System.out.flush();
          ligne = "";
          while ((c=(char)System.in.read()) != '\n') {ligne=ligne+c;}
          canalEcriture.println(ligne);
          canalEcriture.flush();
          // System.out.println("apres flush");
          ligne=canalLecture.readLine();
          // System.out.println("apres readline");
        if (ligne == null) {System.out.println("Connexion terminee"); break;}
        System.out.println("!"+ligne);
        }
      }catch (IOException e) {System.err.println(e);}
      finally { try {if (s != null) s.close();} catch (IOException e2) {} }
  }
}
