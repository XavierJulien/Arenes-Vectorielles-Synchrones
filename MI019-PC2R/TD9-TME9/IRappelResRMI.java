import java.rmi.*;

public interface IRappelResRMI extends Remote {
    void put (Integer result) throws RemoteException;
}
