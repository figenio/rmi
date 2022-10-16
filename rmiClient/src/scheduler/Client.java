package scheduler;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        int portaSN = 1099;
        Scanner userReader = new Scanner(System.in);

        System.out.println("Name yourself:");
        String clientName = userReader.nextLine();
        System.out.println("Client name is: " + clientName);

        Registry namingServiceReference = LocateRegistry.getRegistry(portaSN);
        InterfaceServ serverReference = ((InterfaceServ) namingServiceReference.lookup("scheduler"));
        CliImpl clientReference = new CliImpl(clientName, serverReference);

        while(true) {
            System.out.println("What do you wanna do?");
            System.out.println("1 - Register new appointment");
            System.out.println("2 - Cancel appointment");
            System.out.println("3 - Cancel appointment alert");
            System.out.println("4 - Consult appointments");
            System.out.println("5 - Ping server");
            System.out.println("If nothing, than type anything else.");

//            try {
//                Thread.sleep(2_000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

//            if(!(userReader.hasNext("y") || userReader.hasNext("n"))) {
                String input = userReader.nextLine();
                if (isNumeric(input)) {
                    int option = Integer.parseInt(input);
                    System.out.println("Main inputted: " + option);

                    switch (option) {
                        case 1:
                            clientReference.registerAppointment();
                            break;
                        case 2:
                            clientReference.cancelAppointment();
                            break;
                        case 3:
                            clientReference.cancelAppointmentAlert();
                            break;
                        case 5:
                            clientReference.pingServ();
                            break;
                        default:
                            System.out.println("default route");
                    }
                }
//            }
        }


    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
