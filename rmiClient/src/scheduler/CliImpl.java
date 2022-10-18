package scheduler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {

    Scanner userReader = new Scanner(System.in);
    String clientName;
    Signature clientSignature;

    InterfaceServ serverReference = null;
    PublicKey serverPublicKey;

    protected CliImpl(String clientName, InterfaceServ serverReference) throws RemoteException {
        this.clientName = clientName;
        this.serverReference = serverReference;

        System.out.println("Registering client on server");
        serverPublicKey = this.serverReference.registerClient(clientName, this);
        System.out.println("Server public key: " + serverPublicKey.toString());

        try {
            clientSignature = Signature.getInstance("DSA");
            clientSignature.initVerify(serverPublicKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /* ----- IN METHODS ----- */

    // Simple method for testing purposes
    @Override
    public void notify(String msg, byte[] signature) throws RemoteException {
        try {
            clientSignature.update(msg.getBytes());
            if (clientSignature.verify(signature)) {
                System.out.println("** Validated server message **");
                System.out.println("Received from server: " + msg);
            } else {
                System.out.println("Not validate server message");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inviteToAppointment(String apName, Timestamp apTime, String text, byte[] signature) throws RemoteException {
        try {
            clientSignature.update(text.getBytes());

            // Server signature validation
            if (clientSignature.verify(signature)) {
                System.out.println("** Validated server message **");

                System.out.println("You were invited to " + apName + " at " + apTime.toString());
                System.out.println("Do you accepted? (y/n)");
                String option = userReader.nextLine();
                if (option.startsWith("y")) {
                    System.out.println("Appointment confirmed");
                    serverReference.confirmAppointment(clientName, apName, 5);
                } else if (option.startsWith("n")) {
                    System.out.println("Appointment refused");
                    serverReference.confirmAppointment(clientName, apName, -1);
                }
            } else {
                System.out.println("Not validated server message");
            }
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }


    /* ----- OUT METHODS ----- */

    /* Register Appointment */
    public void registerAppointment() {
        System.out.println("Registering new appointment!");

        System.out.println("Give a name to the new appointment:");
        String appointmentName = userReader.nextLine();
        System.out.println("Set a date and time to the new appointment (format '2022-11-16 23:30:00'):");
        Timestamp appointmentTime = Timestamp.valueOf(userReader.nextLine());
        System.out.println("Set alert for the new appointment (must be int):");
        int alertTime = Integer.parseInt(userReader.nextLine());
        System.out.println("Name guests to the appointment (divide by '-'):");
        String inputtedGuests = userReader.nextLine();
        List<String> guests = List.of(inputtedGuests.split("-"));
        System.out.println("Invited:");
        for (int i = 0; i < guests.size(); i++) {
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

    void queryAppointments() {
        System.out.println("Querying for appointments");

        System.out.println("Give the date to be queried:");
        Timestamp appointmentTime = Timestamp.valueOf(userReader.nextLine());

        try {
            List<String> appointments = this.serverReference.queryAppointments(this.clientName, appointmentTime);

            System.out.println("Printing appointments of day:");
            System.out.println("---");
            for (int i = 0; i < appointments.size(); i++) {
                System.out.println(appointments.get(i));
            }
            System.out.println("---");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
