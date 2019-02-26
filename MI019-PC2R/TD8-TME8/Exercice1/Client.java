/* classe Client pour illustrer la sérialisation d'objets en Java via le réseau */
import java.io.*;
import java.net.*;

public class Client{

    public static void main(String[] argv){
                
        try{
            if (argv.length !=2) {
                System.out.println("Usage : java Client <hostame> <port>");
                System.exit(0);
            }
            String hostname = argv[0];          
            int port = new Integer(argv[1]).intValue();        
                        
            Socket client = new Socket(hostname,port);
            Individu ind = new Individu("Monnom","Monprénom",20);
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(ind);        
            oos.flush();
            oos.close();
            System.out.println("Objet individu envoyé au serveur");
            System.out.println(ind.toString());
        }
        catch(Exception e){e.printStackTrace();}
                
    }
}
