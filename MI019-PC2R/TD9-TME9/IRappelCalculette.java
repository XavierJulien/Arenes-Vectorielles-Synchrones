import java.rmi.*;

public interface IRappelCalculette extends IRemoteCalculette{
 Integer getCumulR(IRappelResRMI resCumul)  throws RemoteException;

}


