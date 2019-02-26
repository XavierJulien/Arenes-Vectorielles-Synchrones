//IRemoteCalculette.java

import java.rmi.*;
public interface  IRemoteCalculette extends Remote{
public Integer eval(Integer op1, Integer op2)  throws RemoteException;
public void cumuler(Integer val)  throws RemoteException;
public Integer getCumul()  throws RemoteException;
public void resetCumul()  throws RemoteException;
}


