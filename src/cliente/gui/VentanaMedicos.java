package cliente.gui;

import cliente.validacionEntradas.ValidarMedico;

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
            JTextField txtEspecialidad = new JTextField();
            JTextField txtCedula = new JTextField();
            JTextField txtCorreo = new JTextField();

            // Etiquetas
            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Especialidad:"));
            panelFormulario.add(txtEspecialidad);
            panelFormulario.add(new JLabel("Cedula:"));
            panelFormulario.add(txtCedula);
            panelFormulario.add(new JLabel("Correo:"));
            panelFormulario.add(txtCorreo);

            // Botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnAgregar = new JButton("Agregar");
            JButton btnCancelar = new JButton("Cancelar");

            btnAgregar.addActionListener((ActionEvent evt) -> {
                String nombre = txtNombre.getText();
                String especialidad = txtEspecialidad.getText();
                String cedula = txtCedula.getText();
                String correo = txtCorreo.getText();

                if (!ValidarMedico.validarNombre(nombre)) {
                    JOptionPane.showMessageDialog(dialog, "Nombre inválido o demasiado largo.");
                    return;
                }
                if (!ValidarMedico.validarEspecialidad(especialidad)) {
                    JOptionPane.showMessageDialog(dialog, "La especialidad debe tener menos de 100 caracteres.");
                    return;
                }
                if (!ValidarMedico.validarCedula(cedula)) {
                    JOptionPane.showMessageDialog(dialog, "La cedula debe tener .-....");
                    return;
                }
                if (!ValidarMedico.validarCorreo(correo)) {
                    JOptionPane.showMessageDialog(dialog, "Correo inválido.");
                    return;
                }

                // Aquí puedes agregar la lógica para agregar el paciente a la tabla
                // Por ahora, solo mostramos un mensaje con los datos ingresados
                JOptionPane.showMessageDialog(dialog, "Nuevo paciente agregado: " + nombre + ", " + especialidad);
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
