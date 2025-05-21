package cliente.gui;

import cliente.validacionEntradas.ValidarPaciente;

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
            // Crear el cuadro de diálogo emergente
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Paciente", true);
            dialog.setSize(300, 250);
            dialog.setLocationRelativeTo(this);

            // Panel para los campos del formulario
            JPanel panelFormulario = new JPanel(new GridLayout(5, 2));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Campos de entrada
            JTextField txtNombre = new JTextField();
            JTextField txtCurp = new JTextField();
            JTextField txtTelefono = new JTextField();
            JTextField txtCorreo = new JTextField();

            // Etiquetas
            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Curp:"));
            panelFormulario.add(txtCurp);
            panelFormulario.add(new JLabel("Teléfono:"));
            panelFormulario.add(txtTelefono);
            panelFormulario.add(new JLabel("Correo:"));
            panelFormulario.add(txtCorreo);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnAgregar = new JButton("Agregar");
            JButton btnCancelar = new JButton("Cancelar");

            btnAgregar.addActionListener((ActionEvent evt) -> {
                String nombre = txtNombre.getText();
                String curp = txtCurp.getText();
                String telefono = txtTelefono.getText();
                String correo = txtCorreo.getText();

                if (!ValidarPaciente.validarNombre(nombre)) {
                    JOptionPane.showMessageDialog(dialog, "Nombre inválido o demasiado largo.");
                    return;
                }
                if (!ValidarPaciente.validarCurp(curp)) {
                    JOptionPane.showMessageDialog(dialog, "CURP debe tener exactamente 18 caracteres.");
                    return;
                }
                if (!ValidarPaciente.validarTelefono(telefono)) {
                    JOptionPane.showMessageDialog(dialog, "Teléfono debe tener 10 dígitos numéricos.");
                    return;
                }
                if (!ValidarPaciente.validarCorreo(correo)) {
                    JOptionPane.showMessageDialog(dialog, "Correo inválido.");
                    return;
                }

                // Aquí puedes agregar la lógica para agregar el paciente a la tabla
                // Por ahora, solo mostramos un mensaje con los datos ingresados
                JOptionPane.showMessageDialog(dialog, "Nuevo paciente agregado: " + nombre + ", " + curp);
                dialog.dispose(); // Cerrar la ventana después de agregar
            });

            // Acción botón "Cancelar"
            btnCancelar.addActionListener((ActionEvent evt) -> {
                dialog.dispose(); // Cerrar la ventana sin hacer nada
            });

            panelBotones.add(btnAgregar);
            panelBotones.add(btnCancelar);

            // Agregar los paneles al cuadro de diálogo
            dialog.add(panelFormulario, BorderLayout.CENTER);
            dialog.add(panelBotones, BorderLayout.SOUTH);

            dialog.setVisible(true);
        });
    }
}
