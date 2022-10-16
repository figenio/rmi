package scheduler;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

public interface InterfaceServ extends Remote {

     void registerClient(String clientName, InterfaceCli clientInterface) throws RemoteException;
     void registerAppointment(String clientName, String apName, Timestamp apTime, List<String> guests, int alertTime) throws RemoteException;
     void confirmAppointment(String clientName, String apName, int alertTime) throws RemoteException;
}