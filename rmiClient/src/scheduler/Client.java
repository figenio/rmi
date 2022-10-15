package scheduler;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        int portaSN = 1099;

        Registry referenciaServicoNomes = LocateRegistry.getRegistry(portaSN);

        InterfaceServ referenciaServidor = ((InterfaceServ) referenciaServicoNomes.lookup("scheduler"));

        CliImpl c = new CliImpl(referenciaServidor);


    }
}
