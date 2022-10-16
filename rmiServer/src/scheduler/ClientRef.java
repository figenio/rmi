package scheduler;

import scheduler.InterfaceCli;

import java.util.ArrayList;
import java.util.List;

public class ClientRef {
    InterfaceCli interfaceCli;
    List<Appointment> appointments;

    public ClientRef(InterfaceCli interfaceCli) {
        this.interfaceCli = interfaceCli;
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
}
