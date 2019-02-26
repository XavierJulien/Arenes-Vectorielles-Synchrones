import java.rmi.*;
import java.rmi.server.*;

public class RappelResRMI extends UnicastRemoteObject
    implements  IRappelResRMI, Unreferenced {

    private Integer result = null;
    private boolean f = false;

    public  RappelResRMI () throws RemoteException {
    }

    public void finish () {
	f=true;
    }

    public boolean isFinished () {
	return f;
    }

    public void put (Integer r) throws RemoteException {
	//allonger le temps de l'operation, 
	//appele par TCalculCumul de terminer
	try {
	    synchronized(this) { wait(10); }
	} catch (InterruptedException ie) {};

	result = r;
	finish();
	System.out.println("rangement resultat " + result.intValue());
	synchronized(this) { this.notifyAll(); }
    }

    public void unreferenced () {
	try {
	    boolean b = UnicastRemoteObject.unexportObject(this, true);
	} catch (NoSuchObjectException nsoe) {}
    }

    public Integer getResult () {
	return result;
    }
}
