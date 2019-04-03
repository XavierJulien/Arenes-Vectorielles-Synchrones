/*****

- A faire d'abord     rmiregistry 1234 &

- Puis

    java ServCalculette2 localhost 1234 calculette
ou
    java -Djava.security.policy=Calculette.policy \
        ServCalculette2 localhost 1234 calculette

- Penser a autoriser dans Calculette.policy le port 1234, sinon, ca plante.

*****/

import java.net.*;
import java.rmi.*;
import java.rmi.server.*;

public class ServCalculette2 { //classe ordinaire, cote serveur
    public static void main (String args[]) {
        String URL;

        if (args.length < 3)
            {URL="rmi://localhost:1234/calculette";
                System.err.println("AlistRegister : usage host port object"
                                   + " par defaut   " + URL);
                //               System.exit(1);
            }

        else { URL = "rmi://"+args[0]+":"+args[1]+"/"+args[2];}
	
	/*
        if ( System.getSecurityManager() == null){
            System.setSecurityManager(new RMISecurityManager());}
        else{ System.out.println("Deja un gestionnaire de Securite, de classe "+
	System.getSecurityManager().getClass().getName());}
	*/
        if ( System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());}
        else{ System.out.println("Deja un gestionnaire de Securite, de classe "+
	System.getSecurityManager().getClass().getName());}
	
        try {
            RemoteCalculette calculatrice = new RemoteCalculette();
            Naming.rebind(URL,calculatrice);
            URL = "rmi://"+args[0]+":"+args[1]+"/calc";
            RemoteCalculette calc = new RemoteCalculette();
            Naming.rebind(URL,calc);
        }
        catch(RemoteException e) {
            System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
            System.exit(1);
        }
        // catch(AlreadyBoundException e) { //Utilise seulement pour bind
        //    System.err.println(URL+": nom deja associe a un objet ");
        //    System.exit(1);
        //}
        catch(MalformedURLException e) {
            System.err.println(URL+" :  URL syntaxiquement incorrecte");
            System.exit(1);
        }
    }
}
