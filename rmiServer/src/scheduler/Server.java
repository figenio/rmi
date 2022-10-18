package scheduler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static void main(String[] args) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException {
        int namingServicePort = 1099;

        Map<String, ClientRef> clients = new HashMap<>();
        Map<String, Appointment> appointments = new HashMap<>(); // Server reference of appointments to alert clients
        Map<String, List<String>> clientsToInvite = new HashMap<>(); // List of clients to invite to appointments (client, appointments[])

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(512);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // Creates and initializes signature
        Signature serverSignature = Signature.getInstance("DSA");
        serverSignature.initSign(privateKey);

        Registry namingServiceReference = LocateRegistry.createRegistry(namingServicePort);
        InterfaceServ serverReference = new ServImpl(clients, appointments, clientsToInvite, publicKey);
        /* Passes appointments and clients to invite references for server implementation to fill it */

        namingServiceReference.rebind("scheduler", serverReference);

        while (true) { // Scheduler validation loop
            try {
                Thread.sleep(15_000);

                // Validates appointments time and sends necessary alerts
                for (Map.Entry<String, Appointment> appointment : appointments.entrySet()) {
                    Timestamp apTime = appointment.getValue().dateTime;
                    Timestamp nowTime = new Timestamp(System.currentTimeMillis());
                    for (Map.Entry<String, Integer> guest : appointment.getValue().guests.entrySet()) {
                        if (minuteComparison(apTime.getTime() - (guest.getValue() * 60 * 1_000), nowTime.getTime())
                                && guest.getValue() > 0)
                            try {
                                String msg = "* Event " + appointment.getKey() + " starting " + guest.getValue() + " minutes from now!!!";
                                byte[] msgBytes = msg.getBytes();
                                serverSignature.update(msgBytes);
                                byte[] signature = serverSignature.sign();

                                clients.get(guest.getKey()).interfaceCli.notify(msg, signature);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            } catch (SignatureException e) {
                                throw new RuntimeException(e);
                            }
                    }
                }

                // Validates if any client needs to be invited to an appointment
                List<String> guestsInvited = new ArrayList<>();
                for (Map.Entry<String, List<String>> client : clientsToInvite.entrySet()) {
                    guestsInvited.add(client.getKey());
                    for (int i = 0; i < client.getValue().size(); i++) {
                        try {
                            byte[] msgBytes = "Invite".getBytes();
                            serverSignature.update(msgBytes);
                            byte[] signature = serverSignature.sign();
                            clients.get(client.getKey()).interfaceCli.inviteToAppointment(client.getValue().get(i), appointments.get(client.getValue().get(i)).dateTime, "Invite", signature); // Asks confirmation for all guests
                        } catch (RemoteException | SignatureException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if (guestsInvited.size() > 0) { // Clears invite list
                    for (int i = 0; i < guestsInvited.size(); i++) {
                        clientsToInvite.remove(guestsInvited.get(i));
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Validation down to the minute
    static boolean minuteComparison(long t1, long t2) {
        Timestamp time1 = new Timestamp(t1);
        Timestamp time2 = new Timestamp(t2);
        if (time1.getYear() == time2.getYear()
                && time1.getMonth() == time2.getMonth()
                && time1.getDate() == time2.getDate()
                && time1.getHours() == time2.getHours()
                && time1.getMinutes() == time2.getMinutes()) {
            return true;
        } else {
            return false;
        }
    }

}
