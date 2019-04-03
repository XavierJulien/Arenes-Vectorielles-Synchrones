import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

public class ServRappelCalculette { //classe ordinaire, cote serveur

    public static void main (String args[]) {
	String URL;

        if (args.length < 3) {
	    URL = "//localhost:1234/calculette";
	    System.err.println("AlistRegister : usage host port object"
			       + " par defaut   " + URL);
	} else {
	    URL = "rmi://"+args[0]+":"+args[1]+"/"+args[2];
	}

	//creation objet
        try {
            RemoteCalculette calculRappel = new RappelCalculette();
            Naming.rebind(URL,calculRappel);
        } catch(RemoteException e) {
            System.err.println(URL + ":  serveur rmi indisponible" + "(" + 
			       e.getMessage() + ")");
	    e.printStackTrace();
            System.exit(1);
        } catch (MalformedURLException e) {
            System.err.println(URL+" :  URL syntaxiquement incorrecte");
            System.exit(1);
        }
    }
}
