package scheduler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    Scanner userReader = new Scanner(System.in);
    String name;

    InterfaceServ referenciaServidor = null;

    protected CliImpl(InterfaceServ referenciaServidor) throws RemoteException {
        this.referenciaServidor = referenciaServidor;
        this.referenciaServidor.registerClient("Client 1", this);
    }


    /* ALERTA DE EVENTO
    Recebe info do compromisso
    * */
    @Override
    public void notify(String texto) throws RemoteException {
        System.out.println("Recebido do servidor: " + texto);
    }

    @Override
    public void inviteToAppointment(String apName, Timestamp apTime) throws RemoteException {
        System.out.println("You were invited to " + apName + " at " + apTime.toString());
        System.out.println("Do you accepted? (y/n)");
        String option = userReader.nextLine();
        if (option.startsWith("y")) {
            System.out.println("What time is alert to be set?");
            int time = userReader.nextInt();
            referenciaServidor.confirmAppointment(name, apName, time);
        }

    }

    /* CONVITE DE EVENTO
    * Recebe info de compromisso e o usuário responde se tem ou não interesse e o horário do alerta que quer*/


}
