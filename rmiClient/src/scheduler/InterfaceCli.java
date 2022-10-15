package scheduler;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {

    public void notify(String texto) throws RemoteException;
}
