package scheduler;// Classe que defini o que é compromisso

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appointment {
//    String name; // Name of appointment (identifier)
    Timestamp dateTime; // Date and time of appointment
    Map<String, Integer> guests; // List of guests

    public Appointment(Timestamp dateTime) {
//        this.name = name;
        this.dateTime = dateTime;
        this.guests = new HashMap<>();

//        for (int i = 0 ; i< guests.size(); i++) {
//            this.guests.put(guests.get(i), 0);
//        }
    }

    // Adds a guest to the appointment
    void addGuest(String clientName, int time) {
        guests.put(clientName, time);
    }

    // Changes guest alert of the appointment
    void resetGuestAlert(String clientName, int time) {
        guests.put(clientName, time);
    }

    // Removes guest from the appointment
    void removeGuest(String clientName) {
        guests.remove(clientName);
    }

}

//class Guest {
//    String name; // Client name (identifier)
//    int alertTime; // Client desired alert time
//
//    public Guest(String name, int alertTime) {
//        this.name = name;
//        this.alertTime = alertTime;
//    }
//}
