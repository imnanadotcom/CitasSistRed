import cliente.gui.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal app = new VentanaPrincipal();
            app.setVisible(true);
        });
    }
}