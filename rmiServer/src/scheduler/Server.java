package scheduler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        int portaSN = 1099;

        try {
            Registry referenciaServicoNomes = LocateRegistry.createRegistry(portaSN);

            InterfaceServ referenciaServidor = new ServImpl();

            referenciaServicoNomes.rebind("scheduler", referenciaServidor);

        }catch(RemoteException exception){


        }
    }
}
