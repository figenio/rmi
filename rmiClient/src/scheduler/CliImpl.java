package scheduler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    InterfaceServ referenciaServidor = null;

    protected CliImpl(InterfaceServ referenciaServidor) throws RemoteException {
        this.referenciaServidor = referenciaServidor;
        this.referenciaServidor.registerInterest("Oi", this);
    }


    /* ALERTA DE EVENTO
    Recebe info do compromisso
    * */
    @Override
    public void notify(String texto) throws RemoteException {
        System.out.println("Recebido do servidor: " + texto);
    }

    /* CONVITE DE EVENTO
    * Recebe info de compromisso e o usuário responde se tem ou não interesse e o horário do alerta que quer*/


}
