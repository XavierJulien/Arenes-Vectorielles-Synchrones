import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class RemoteCalculette  extends UnicastRemoteObject // Objets distants
    implements IRemoteCalculette{

    // static final long serialVersionUID = 1L;
    
    int cumul = 0;
    
    public RemoteCalculette()  throws RemoteException{super();} 
    public Integer eval(Integer op1, Integer op2)  throws RemoteException{
        return new Integer(op1.intValue() + op1.intValue()) ;
    }
    public void cumuler(Integer val)  throws RemoteException {
        cumul += val.intValue();
    }
    public Integer getCumul()  throws RemoteException {
        return new Integer(cumul);
    }
    public void resetCumul()  throws RemoteException {
        cumul = 0;
    }
}
