package scheduler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    Scanner userReader = new Scanner(System.in);
    String clientName;

    InterfaceServ serverReference = null;

    protected CliImpl(String clientName, InterfaceServ serverReference) throws RemoteException {
        this.clientName = clientName;
        this.serverReference = serverReference;
        System.out.println("Registering client on server");
        this.serverReference.registerClient(clientName, this);
    }

    /* ----- IN METHODS ----- */

    // Simple method for testing purposes
    @Override
    public void notify(String text) throws RemoteException {
        System.out.println("Received from server: " + text);
    }

    // Receives server public key as confirmation of client registration
    @Override
    public void registeringConfirmation(String text) {
        System.out.println("Client registration successful!");
        System.out.println("Server key is: " + text);
    }

    @Override
    public void inviteToAppointment(String apName, Timestamp apTime) throws RemoteException {
        System.out.println("You were invited to " + apName + " at " + apTime.toString());
        System.out.println("Do you accepted? (y/n)");
        String option = userReader.nextLine();
        if (option.startsWith("y")) {
            System.out.println("What time is alert to be set? (set 0 if no alarm is needed)");
            int time = Integer.parseInt(userReader.nextLine());
            serverReference.confirmAppointment(clientName, apName, time);
        } else if (option.startsWith("n")) {
            System.out.println("Appointment refused");
            serverReference.confirmAppointment(clientName, apName, -1);
        }

    }

    /* CONVITE DE EVENTO
    * Recebe info de compromisso e o usuário responde se tem ou não interesse e o horário do alerta que quer*/



    /* ----- OUT METHODS ----- */

    // Simple method for testing purposes
    public void pingServ() {
        try {
            this.serverReference.pingServer(this.clientName, this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /* Register Appointment */
    public void registerAppointment() {
        System.out.println("Registering new appointment!");

        System.out.println("Give a name to the new appointment:");
        String appointmentName = userReader.nextLine();
        System.out.println("Set a date and time to the new appointment (format '2022-11-11 12:00:00'):");
        Timestamp appointmentTime = Timestamp.valueOf(userReader.nextLine());
        System.out.println("Set alert for the new appointment (must be int):");
        int alertTime = Integer.parseInt(userReader.nextLine());
        System.out.println("Name guests to the appointment (divide by '-'):");
        String inputtedGuests = userReader.nextLine();
        List<String> guests = List.of(inputtedGuests.split("-"));
        System.out.println("Invited:");
        for (int i = 0 ; i< guests.size(); i++) {
            System.out.println(guests.get(i));
        }
        // Call server
        try {
            this.serverReference.registerAppointment(this.clientName, appointmentName, appointmentTime, guests, alertTime);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /* Cancel Appointment */
    void cancelAppointment() {
        System.out.println("Cancelling appointment!");

        System.out.println("Give the name of the appointment to be cancelled:");
        String appointmentName = userReader.nextLine();
        try {
            this.serverReference.cancelAppointment(this.clientName, appointmentName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /* Cancel Appointment Alert */
    void cancelAppointmentAlert() {
        System.out.println("Cancelling appointment alert!");

        System.out.println("Give the name of the appointment alert to be cancelled:");
        String appointmentName = userReader.nextLine();
        try {
            this.serverReference.cancelAppointmentAlert(this.clientName, appointmentName);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /* Consult Appointments */


}
