package scheduler;// Classe que defini o que é compromisso

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Appointment {
//    String name; // Name of appointment (identifier on Map)
    Timestamp dateTime; // Date and time of appointment
    Map<String, Integer> guests; // List of guests and their preferred alert time

    public Appointment(Timestamp dateTime) {
//        this.name = name;
        this.dateTime = dateTime;
        this.guests = new HashMap<>();
    }

    // Adds a guest to the appointment and its alert
    void addGuest(String clientName, int time) {
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
