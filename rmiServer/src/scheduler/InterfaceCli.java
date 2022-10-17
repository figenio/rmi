package scheduler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Signature;
import java.sql.Timestamp;

public interface InterfaceCli extends Remote {

    public void notify(String text, byte[] signature) throws RemoteException;
//    public void registeringConfirmation(String text) throws RemoteException;
    public void inviteToAppointment(String apName, Timestamp apTime, String text, byte[] signature) throws RemoteException;
}
