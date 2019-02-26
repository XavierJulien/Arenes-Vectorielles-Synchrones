import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;

/**
cree un registre rmi, y enregistre un objet IRemoteCalculette de nom calc
attend le numero de port en parametre ligne commande, defaut 1234
*/
public class ServAutonomeCalculette { //classe ordinaire, cote serveur
    public static void main (String args[]) {
	String URL;int port;

	try {
	    if (args.length>0){port=Integer.valueOf(args[0]).intValue();}
	    else {port =1234;}
	    Registry reg= LocateRegistry.createRegistry(port);

 	    RemoteCalculette calculatrice = new RemoteCalculette();
	    reg.rebind("calc",calculatrice);
	    System.out.println("apres bind");
	    /*
	    IRemoteCalculette calc = (IRemoteCalculette)
		reg.lookup("calc");
	    System.out.println("apres lookup");
	    System.out.println("directe " +
		      calculatrice.eval(new Integer(5),new Integer(5)) + " " +
	calc.eval(new Integer(5),new Integer(5)));
	    */
 //System.out.println( calculatrice.getClientHost());

//les groupes de threads

	}
	catch(RemoteException e) {
	    System.err.println(":  serveur rmi indisponible"+"("
			       + e.getMessage()+")");
	    System.exit(1);
	}
	catch(Exception e) {
	    System.err.println(":  serveur rmi indisponible"+"("+
			       e.getMessage()+")");
	    System.exit(1);
	}
    }
}

/* inutiles ici :

	catch(AlreadyBoundException e) {
	    System.err.println(": nom deja associe a un objet ");
	    System.exit(1);
	}
	catch(MalformedURLException e) {
	    System.err.println(" :  URL syntaxiquement incorrecte");
	    System.exit(1);
	    }
*/
