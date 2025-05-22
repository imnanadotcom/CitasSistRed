package Servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class ServidorMain {

    public static void main(String[] args) {
        try {
            ServidorOperacionesImpl servicioCitas = new ServidorOperacionesImpl();

            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("RMI Registry creado en el puerto 1099.");
            } catch (RemoteException e) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("RMI Registry ya existente en el puerto 1099 conectado.");
            }

            registry.rebind("ServicioCitas", servicioCitas);

            System.out.println("Servidor de Citas RMI listo. Servicio 'ServicioCitas' registrado.");
            System.out.println("Presiona Ctrl+C para detener el servidor.");

        } catch (RemoteException e) {
            System.err.println("Excepción del Servidor RMI (RemoteException): " + e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Excepción del Servidor RMI (General): " + e.toString());
            e.printStackTrace();
        }
    }
}