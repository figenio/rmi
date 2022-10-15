package scheduler;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServ extends Remote {

     void registerInterest (String texto, InterfaceCli referenciaCliente) throws RemoteException;
}