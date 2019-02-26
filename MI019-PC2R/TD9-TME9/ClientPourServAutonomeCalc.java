import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;
public class ClientPourServAutonomeCalc { //classe ordinaire, cote serveur
    public static void main (String args[]) {
 String URL="";
 int port;
 
	try {
	    if (args.length>0){port=Integer.valueOf(args[0]).intValue();}
	    else {port =1234;}

	    Registry reg= LocateRegistry.getRegistry(port);


	    IRemoteCalculette calc = (IRemoteCalculette)
		reg.lookup("/calc");
	    System.out.println("apres lookup");
	    System.out.println("apres eval " +
			       calc.eval(new Integer(5),new Integer(5)));


	}
	catch(RemoteException e) {
	    System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
	    System.exit(1);
	}
	
	catch(Exception e) {
	    System.err.println(URL+":  serveur rmi indisponible"+"("+e.getMessage()+")");
	    System.exit(1);
	}
    }
}
