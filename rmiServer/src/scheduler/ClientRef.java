package scheduler;

import scheduler.InterfaceCli;

import java.util.ArrayList;
import java.util.List;

public class ClientRef {
    InterfaceCli interfaceCli;
    List<String> appointments;

    public ClientRef(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(String appointmentName) {
        appointments.add(appointmentName);
    }
}