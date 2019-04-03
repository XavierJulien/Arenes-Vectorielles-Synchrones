import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
public class ServCalculette { //classe ordinaire, cote serveur
    public static void main (String args[]) {
        String URL;

        if (args.length < 3)
            {URL="rmi://localhost:1234/calculette";
                System.err.println("AlistRegister : usage host port object"
                                   + " par defaut   " + URL);
                //               System.exit(1);
            }

        else { URL = "rmi://"+args[0]+":"+args[1]+"/"+args[2];} 

        try {
            RemoteCalculette calculatrice = new RemoteCalculette();
            Naming.bind(URL,calculatrice);
        }
        catch(RemoteException e) {
            System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
            System.exit(1);
        }
        catch(AlreadyBoundException e) {
            System.err.println(URL+": nom deja associe a un objet ");
            System.exit(1);
        }
        catch(MalformedURLException e) {
            System.err.println(URL+" :  URL syntaxiquement incorrecte");
            System.exit(1);
        }
    }
}
