package cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Servidor.interfaz.ServicioCitasRMI;
import cliente.gui.VentanaPrincipal;

public class ClienteMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();

            ServicioCitasRMI service = (ServicioCitasRMI) registry.lookup("ServicioCitas");

            System.out.println("Cliente conectado a servidor");

            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(service);
            ventanaPrincipal.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}