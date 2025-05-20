package cliente.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPacientes extends JPanel {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;

    public VentanaPacientes() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón Nuevo paciente
        JButton btnNuevoPaciente = new JButton("Nuevo paciente");
        btnNuevoPaciente.setBackground(Color.DARK_GRAY);
        btnNuevoPaciente.setForeground(Color.WHITE);
        btnNuevoPaciente.setFocusPainted(false);
        btnNuevoPaciente.setPreferredSize(new Dimension(150, 30));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnNuevoPaciente);
        add(panelBoton, BorderLayout.NORTH);

        // Etiqueta pacientes registrados
        JLabel lblPacientesRegistrados = new JLabel("Pacientes registrados");
        lblPacientesRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblPacientesRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblPacientesRegistrados, BorderLayout.CENTER);

        // Crear modelo de la tabla y agregar algunas filas de ejemplo
        String[] columnas = {"ID", "Nombre", "Curp", "Teléfono", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPacientes = new JTable(modeloTabla);

        // Agregar algunas filas de prueba
        modeloTabla.addRow(new Object[]{"1", "Juan Pérez", "JUAN1234", "555-1234", "juan@example.com"});
        modeloTabla.addRow(new Object[]{"2", "Ana Gómez", "ANA5678", "555-5678", "ana@example.com"});

        // Añadir JScrollPane a la tabla y agregarlo al panel
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        add(scrollPane, BorderLayout.SOUTH);

        // Acción del botón "Nuevo paciente"
        btnNuevoPaciente.addActionListener((ActionEvent e) -> {
            // Abrir ventana emergente para agregar un nuevo paciente
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Paciente", true);
            dialog.setSize(300, 150);
            dialog.setLocationRelativeTo(this);
            JLabel etiqueta = new JLabel("Formulario para nuevo paciente");
            etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
            dialog.add(etiqueta);
            dialog.setVisible(true);
        });
    }
}
