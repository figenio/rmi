package scheduler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public interface InterfaceCli extends Remote {

    public void notify(String texto) throws RemoteException;
    public void inviteToAppointment(String apName, Timestamp apTime) throws RemoteException;
}
