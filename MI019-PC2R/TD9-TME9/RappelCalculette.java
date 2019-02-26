import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

//inchangee pour rappel
public class RappelCalculette  extends RemoteCalculette
    implements IRappelCalculette {

    Vector vals = new Vector(200,100);
    int    k    = 0;

    public RappelCalculette () throws RemoteException {
	super();
    } 

    public void cumuler (Integer val)
	throws RemoteException {

	vals.add(k++, val);
    }
    
    //redefinie pour simuler un calcul 'plus long'
    public Integer getCumul() throws RemoteException {
	int i;
	int r = 0;

	for (i = 0; i < k; i++) {
	    r += ((Integer)vals.elementAt(i)).intValue();
	}
	cumul = r; 
	return new Integer(r);
    }

    //avec l'objet de rappel
    public Integer getCumulR (IRappelResRMI res)
	throws RemoteException {

	//lance le calcul dans un thread separe
	new TCalculCumul (this, res).start();
	System.out.println("lancement Thread ");
	Thread.yield();
	return new Integer(0);
    }

    //redefinie pour coherence
    public void resetCumul () throws RemoteException {
	super.resetCumul();

	for (int i=0; i<k;i++) {
	    vals.add(i, null);
	}
	k = 0;
    }
}
