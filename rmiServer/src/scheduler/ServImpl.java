package scheduler;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    Map<String, ClientRef> clients;
    Map<String, Appointment> appointments;

    protected ServImpl() throws RemoteException {
        clients = new HashMap<>();
        appointments = new HashMap<>();
    }

    @Override
    public void registerClient(String clientName, InterfaceCli clientInterface) {
        clients.put(clientName, new ClientRef(clientInterface));
        System.out.println("New client: " + clientName);
        try {
            clients.get(clientName).interfaceCli.notify("Registered"); // Should return public key
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Register a new appointment and send guests their invitation
    @Override
    public void registerAppointment(String clientName, String apName, Timestamp apTime, List<String> guests, int alertTime) {
        appointments.put(apName, new Appointment(apTime, guests)); // Register appointment
        this.confirmAppointment(clientName, apName, alertTime); // Confirm appointment for its creator
        for (int i = 0 ; i< guests.size(); i++) {
            try {
                clients.get(guests.get(i)).interfaceCli.inviteToAppointment(apName, apTime); // Asks confirmation for all guests
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Client confirms interest in appointment and sets appointment alert
    @Override
    public void confirmAppointment(String clientName, String apName, int alertTime) {
        appointments.get(apName).resetGuestAlert(clientName, alertTime);
        clients.get(clientName).addAppointment(appointments.get(apName));
    }

    /* CANCELAMENTO DE COMPROMISSO
    * Cliente informa o nome do compromisso a ser cancelado
    * */

    /* CANCELAMENTO DE ALERTA
     * Cliente informa o nome do compromisso a ter o alerta cancelado
     * */

    /* CONSULTA
    * Consulta por dia e retorna todos do dia. */
}
