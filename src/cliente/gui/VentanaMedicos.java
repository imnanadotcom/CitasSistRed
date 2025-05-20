package cliente.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaMedicos extends JPanel {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;

    public VentanaMedicos() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón Nuevo paciente
        JButton btnNuevoPaciente = new JButton("Nuevo Médico");
        btnNuevoPaciente.setBackground(Color.DARK_GRAY);
        btnNuevoPaciente.setForeground(Color.WHITE);
        btnNuevoPaciente.setFocusPainted(false);
        btnNuevoPaciente.setPreferredSize(new Dimension(150, 30));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnNuevoPaciente);

        add(panelBoton, BorderLayout.NORTH);

        // Etiqueta pacientes registrados
        JLabel lblPacientesRegistrados = new JLabel("Médicos registrados");
        lblPacientesRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblPacientesRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblPacientesRegistrados, BorderLayout.CENTER);

        // Tabla de pacientes
        String[] columnas = {"ID", "Nombre", "Especialidad", "Cédula", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        add(scrollPane, BorderLayout.SOUTH);

        // Acción botón nuevo paciente: abre ventana emergente vacía
        btnNuevoPaciente.addActionListener((ActionEvent e) -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Médico", true);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(this);
            JLabel etiqueta = new JLabel("Formulario para nuevo médico");
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            dialog.add(etiqueta);
            dialog.setVisible(true);
        });
    }
}
