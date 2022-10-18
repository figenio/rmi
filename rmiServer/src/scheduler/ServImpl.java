package scheduler;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    Map<String, ClientRef> clients;
    Map<String, Appointment> appointments;
    Map<String, List<String>> clientsToInvite;

    PublicKey publicKey;

    protected ServImpl(Map<String, ClientRef> clients, Map<String, Appointment> appointments, Map<String, List<String>> clientsToInvite, PublicKey publicKey) throws RemoteException {
        this.clients = clients;
        this.appointments = appointments;
        this.clientsToInvite = clientsToInvite;
        this.publicKey = publicKey;
    }

    /* ----- IN METHODS ----- */

    // Registers new client and returns server public key
    @Override
    public PublicKey registerClient(String clientName, InterfaceCli clientInterface) {
        clients.put(clientName, new ClientRef(clientInterface));
        System.out.println("New client: " + clientName);
        return this.publicKey;
    }

    // Register a new appointment and send guests their invitation
    @Override
    public void registerAppointment(String clientName, String apName, Timestamp apTime, List<String> guests, int alertTime) {
        System.out.println("Registering new appointment!");
        appointments.put(apName, new Appointment(apTime)); // Register appointment
        this.confirmAppointment(clientName, apName, alertTime); // Confirm appointment for its creator


        for (int i = 0 ; i< guests.size(); i++) {
            List<String> clientInvitations = new ArrayList<>();
            if (clientsToInvite.containsKey(guests.get(i)) && !clientsToInvite.get(guests.get(i)).isEmpty()) {
                clientInvitations = clientsToInvite.get(guests.get(i));
            }
            clientInvitations.add(apName);
            clientsToInvite.put(guests.get(i), clientInvitations);
//           clients.get(guests.get(i)).interfaceCli.inviteToAppointment(apName, apTime); // Asks confirmation for all guests

        }
    }

    // Client confirms interest in appointment and sets appointment alert
    @Override
    public void confirmAppointment(String clientName, String apName, int alertTime) {
        System.out.println("Confirmation from " + clientName + " for " + apName);
        if (alertTime >= 0) {
            appointments.get(apName).addGuest(clientName, alertTime);
            clients.get(clientName).addAppointment(apName);
        }
    }

    @Override
    public void cancelAppointment(String clientName, String apName) {
        System.out.println("Cancelling " + apName + " for " + clientName);
        clients.get(clientName).appointments.remove(apName);
        appointments.get(apName).removeGuest(clientName);

        // If no more guest are confirmed, the appointment is removed
        if (appointments.get(apName).guests.size() < 1) {
            System.out.println("No more guest for " + apName + ", deleting it!!");
            appointments.remove(apName);
        }
    }

    @Override
    public void cancelAppointmentAlert(String clientName, String apName) {
        System.out.println("Cancelling alert of " + apName + " for " + clientName);
        appointments.get(apName).guests.put(clientName, 0);
    }

    @Override
    public List<String> queryAppointments(String clientName, Timestamp dateToSearch) {
        System.out.println("Querying for appointments of " + clientName);

        List<String> appointmentNames = new ArrayList<>();
        for (Map.Entry<String, Appointment> entry : appointments.entrySet()) {
            if (entry.getValue().dateTime.getDate() == dateToSearch.getDate()
            && entry.getValue().dateTime.getMonth() == dateToSearch.getMonth()
            && entry.getValue().dateTime.getYear() == dateToSearch.getYear()
            && entry.getValue().guests.containsKey(clientName)) {
                appointmentNames.add(entry.getKey());
            }
        }
        System.out.println("Returning appointments found");
        return appointmentNames;
    }
}
