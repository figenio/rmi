package scheduler;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static void main(String[] args) throws RemoteException {
        int namingServicePort = 1099;
        Map<String, Appointment> appointments = new HashMap<>();
        Map<String, List<String>> clientsToInvite = new HashMap<>();

        Registry namingServiceReference = LocateRegistry.createRegistry(namingServicePort);
        InterfaceServ serverReference = new ServImpl(appointments, clientsToInvite);

        namingServiceReference.rebind("scheduler", serverReference);

        while (true) { // Scheduler validation loop
            try {
                Thread.sleep(15_000);

                for (Map.Entry<String, Appointment> appointment : appointments.entrySet()) {
                    // Appointment validation
                    Timestamp apTime = appointment.getValue().dateTime;
                    Timestamp nowTime = new Timestamp(System.currentTimeMillis());
                    for (Map.Entry<String, Integer> guest : appointment.getValue().guests.entrySet()) {
                        if (minuteComparison(apTime.getTime() - (guest.getValue() * 60 * 1_000), nowTime.getTime())
                                && guest.getValue() > 0)
                            serverReference.notify(guest.getKey(), "* Event " + appointment.getKey() + " starting " + guest.getValue() + " minutes from now!!!");
                    }
                }

                for (Map.Entry<String, List<String>> client : clientsToInvite.entrySet()) {
                    for (int i = 0 ; i< client.getValue().size(); i++) {
                        serverReference.inviteClient(client.getKey(), client.getValue().get(i));
                    }
                }
                clientsToInvite = new HashMap<>();
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
