package cliente.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Interfaz extends JFrame {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;

    public Interfaz() {
        setTitle("Sistema de citas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel lateral
        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(Color.LIGHT_GRAY);
        panelLateral.setPreferredSize(new Dimension(150, 0));
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("sistema de citas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLateral.add(lblTitulo);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnPacientes = new JButton("pacientes");
        JButton btnMedicos = new JButton("médicos");
        JButton btnCitas = new JButton("citas");

        // Establecer tamaño fijo para los botones (ancho y alto igual)
        Dimension btnSize = new Dimension(200, 40);
        btnPacientes.setMaximumSize(btnSize);
        btnMedicos.setMaximumSize(btnSize);
        btnCitas.setMaximumSize(btnSize);

        panelLateral.add(btnPacientes);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelLateral.add(btnMedicos);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelLateral.add(btnCitas);

        add(panelLateral, BorderLayout.WEST);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(Color.WHITE);

        // Botón Nuevo paciente
        JButton btnNuevoPaciente = new JButton("Nuevo paciente");
        btnNuevoPaciente.setBackground(Color.DARK_GRAY);
        btnNuevoPaciente.setForeground(Color.WHITE);
        btnNuevoPaciente.setFocusPainted(false);
        btnNuevoPaciente.setPreferredSize(new Dimension(150, 30));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnNuevoPaciente);

        panelPrincipal.add(panelBoton, BorderLayout.NORTH);

        // Etiqueta pacientes registrados
        JLabel lblPacientesRegistrados = new JLabel("pacientes registrados");
        lblPacientesRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblPacientesRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panelPrincipal.add(lblPacientesRegistrados, BorderLayout.CENTER);

        // Tabla de pacientes
        String[] columnas = {"id", "nombre", "curp", "teléfono", "correo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        panelPrincipal.add(scrollPane, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);

        // Acción botón nuevo paciente: abre ventana emergente vacía
        btnNuevoPaciente.addActionListener((ActionEvent e) -> {
            JDialog dialog = new JDialog(this, "Nuevo Paciente", true);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(this);
            JLabel etiqueta = new JLabel("Formulario para nuevo paciente");
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            dialog.add(etiqueta);
            dialog.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz app = new Interfaz();
            app.setVisible(true);
        });
    }
}
