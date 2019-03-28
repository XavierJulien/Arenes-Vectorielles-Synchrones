import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
  protected static final int PORT=2020;

public static void main(String[] args) {
    Socket s=null;
    if (args.length != 1) {
      System.err.println("Usage: java Client <hote>");
      System.exit(1); }
      try {
        s = new Socket (InetAddress.getByName("127.0.0.1"),PORT);
        BufferedReader canalLecture = new BufferedReader(new InputStreamReader(s.getInputStream()));
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
          System.out.println("apres flush");
          ligne=canalLecture.readLine();
          System.out.println("apres readline");
        if (ligne == null) {System.out.println("Connexion terminee"); break;}
        System.out.println("!"+ligne);
        }
      }catch (IOException e) {System.err.println(e);}
      finally { try {if (s != null) s.close();} catch (IOException e2) {} }
  }
}
