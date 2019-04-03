import java.io.*;
import java.net.*;

public class Serveur{
    public static void main(String[] argv){
        try{
            if (argv.length !=1) {
                System.out.println("Usage : java Serveur <port>");
                System.exit(0);
            }
            int port = new Integer(argv[0]).intValue();        
            ServerSocket serv = new ServerSocket(port);
            System.out.println("Serveur en attente sur le port " + port);
            while (true){
                Socket client = serv.accept();
                ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                Individu ind = (Individu) ois.readObject();        
                ois.close();
                System.out.println("Objet individu reçu par le serveur :");
                System.out.println(ind.toString());
                client.close();
            }
        }
        catch(Exception e){e.printStackTrace();}
    }
}
