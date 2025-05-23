package cliente.gui;

import cliente.validacionEntradas.ValidarMedico;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Servidor.interfaz.ServicioCitasRMI;

import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaMedicos extends JPanel {

    private JTable tablaMedicos;
    private DefaultTableModel modeloTabla;
    private JButton btnEditar;
    private JButton btnEliminar;
    private ServicioCitasRMI service;

    public VentanaMedicos(ServicioCitasRMI service) {

        this.service = service;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botones arriba
        JButton btnNuevoMedico = new JButton("Nuevo Médico");
        btnNuevoMedico.setBackground(Color.DARK_GRAY);
        btnNuevoMedico.setForeground(Color.WHITE);
        btnNuevoMedico.setFocusPainted(false);
        btnNuevoMedico.setPreferredSize(new Dimension(150, 30));

        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnNuevoMedico);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.NORTH);

        // Etiqueta arriba de la tabla
        JLabel lblMedicosRegistrados = new JLabel("Médicos registrados");
        lblMedicosRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblMedicosRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Modelo y tabla
        String[] columnas = {"ID", "Nombre", "Especialidad", "Cédula", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaMedicos = new JTable(modeloTabla);

        // Datos de prueba
        modeloTabla.addRow(new Object[]{"1", "Dr. Juan Pérez", "Cardiología", "CED123456", "juan.perez@example.com"});
        modeloTabla.addRow(new Object[]{"2", "Dra. Ana Gómez", "Pediatría", "CED654321", "ana.gomez@example.com"});

        JScrollPane scrollPane = new JScrollPane(tablaMedicos);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblMedicosRegistrados, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Listener para selección en tabla
        tablaMedicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean filaSeleccionada = tablaMedicos.getSelectedRow() != -1;
                    btnEditar.setEnabled(filaSeleccionada);
                    btnEliminar.setEnabled(filaSeleccionada);
                }
            }
        });

        // Acción botón Nuevo Médico
        btnNuevoMedico.addActionListener((ActionEvent e) -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Médico", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField txtNombre = new JTextField();
            JTextField txtEspecialidad = new JTextField();
            JTextField txtCedula = new JTextField();
            JTextField txtCorreo = new JTextField();

            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Especialidad:"));
            panelFormulario.add(txtEspecialidad);
            panelFormulario.add(new JLabel("Cédula:"));
            panelFormulario.add(txtCedula);
            panelFormulario.add(new JLabel("Correo:"));
            panelFormulario.add(txtCorreo);

            JPanel panelBotonesDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnAgregar = new JButton("Agregar");
            JButton btnCancelar = new JButton("Cancelar");
            panelBotonesDialog.add(btnAgregar);
            panelBotonesDialog.add(btnCancelar);

            dialog.add(panelFormulario, BorderLayout.CENTER);
            dialog.add(panelBotonesDialog, BorderLayout.SOUTH);

            btnAgregar.addActionListener(ev -> {
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
                    JOptionPane.showMessageDialog(dialog, "Cédula inválida.");
                    return;
                }
                if (!ValidarMedico.validarCorreo(correo)) {
                    JOptionPane.showMessageDialog(dialog, "Correo inválido.");
                    return;
                }

                // Agregar médico a la tabla
                int nuevoId = modeloTabla.getRowCount() + 1;
                modeloTabla.addRow(new Object[]{String.valueOf(nuevoId), nombre, especialidad, cedula, correo});

                JOptionPane.showMessageDialog(dialog, "Médico agregado correctamente.");
                dialog.dispose();
            });

            btnCancelar.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        // Acción botón Editar
        btnEditar.addActionListener(ev -> {
            int fila = tablaMedicos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un médico para editar.");
                return;
            }

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Médico", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField txtNombre = new JTextField((String) modeloTabla.getValueAt(fila, 1));
            JTextField txtEspecialidad = new JTextField((String) modeloTabla.getValueAt(fila, 2));
            JTextField txtCedula = new JTextField((String) modeloTabla.getValueAt(fila, 3));
            JTextField txtCorreo = new JTextField((String) modeloTabla.getValueAt(fila, 4));

            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Especialidad:"));
            panelFormulario.add(txtEspecialidad);
            panelFormulario.add(new JLabel("Cédula:"));
            panelFormulario.add(txtCedula);
            panelFormulario.add(new JLabel("Correo:"));
            panelFormulario.add(txtCorreo);

            JPanel panelBotonesDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnGuardar = new JButton("Guardar");
            JButton btnCancelarEditar = new JButton("Cancelar");
            panelBotonesDialog.add(btnGuardar);
            panelBotonesDialog.add(btnCancelarEditar);

            dialog.add(panelFormulario, BorderLayout.CENTER);
            dialog.add(panelBotonesDialog, BorderLayout.SOUTH);

            btnGuardar.addActionListener(ev2 -> {
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
                    JOptionPane.showMessageDialog(dialog, "Cédula inválida.");
                    return;
                }
                if (!ValidarMedico.validarCorreo(correo)) {
                    JOptionPane.showMessageDialog(dialog, "Correo inválido.");
                    return;
                }

                modeloTabla.setValueAt(nombre, fila, 1);
                modeloTabla.setValueAt(especialidad, fila, 2);
                modeloTabla.setValueAt(cedula, fila, 3);
                modeloTabla.setValueAt(correo, fila, 4);

                JOptionPane.showMessageDialog(dialog, "Médico actualizado correctamente.");
                dialog.dispose();
            });

            btnCancelarEditar.addActionListener(ev2 -> dialog.dispose());

            dialog.setVisible(true);
        });

        // Acción botón Eliminar
        btnEliminar.addActionListener(ev -> {
            int fila = tablaMedicos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un médico para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este médico?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modeloTabla.removeRow(fila);
            }
        });
    }
}
