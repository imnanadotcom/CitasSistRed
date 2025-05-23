package cliente.gui;

import cliente.validacionEntradas.ValidarPaciente;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import common.PacienteDTO;
import common.interfaz.ServicioCitasRMI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

public class VentanaPacientes extends JPanel {

    private JTable tablaPacientes;
    private DefaultTableModel modeloTabla;
    private JButton btnEditar;
    private JButton btnEliminar;
    private ServicioCitasRMI service;

    public VentanaPacientes(ServicioCitasRMI service) {

        this.service = service;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnNuevoPaciente = new JButton("Nuevo paciente");
        btnNuevoPaciente.setBackground(Color.DARK_GRAY);
        btnNuevoPaciente.setForeground(Color.WHITE);
        btnNuevoPaciente.setFocusPainted(false);
        btnNuevoPaciente.setPreferredSize(new Dimension(150, 30));

        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnNuevoPaciente);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.NORTH);

        JLabel lblPacientesRegistrados = new JLabel("Pacientes registrados");
        lblPacientesRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblPacientesRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] columnas = {"ID", "Nombre", "Curp", "Teléfono", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblPacientesRegistrados, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        tablaPacientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean filaSeleccionada = tablaPacientes.getSelectedRow() != -1;
                    btnEditar.setEnabled(filaSeleccionada);
                    btnEliminar.setEnabled(filaSeleccionada);
                }
            }
        });

        cargarPacientes();

        btnNuevoPaciente.addActionListener((ActionEvent e) -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nuevo Paciente", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField txtNombre = new JTextField();
            JTextField txtCurp = new JTextField();
            JTextField txtTelefono = new JTextField();
            JTextField txtCorreo = new JTextField();

            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Curp:"));
            panelFormulario.add(txtCurp);
            panelFormulario.add(new JLabel("Teléfono:"));
            panelFormulario.add(txtTelefono);
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

                try {
                    PacienteDTO nuevoPaciente = new PacienteDTO(0, nombre, curp, telefono, correo);
                    service.agregarPaciente(nuevoPaciente);
                    JOptionPane.showMessageDialog(dialog, "Paciente agregado correctamente.");
                    dialog.dispose();
                    cargarPacientes();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error RMI al agregar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error inesperado al agregar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            btnCancelar.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        btnEditar.addActionListener(ev -> {
            int fila = tablaPacientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un paciente para editar.");
                return;
            }

            int idPaciente = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Paciente", true);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(this);

            JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 5, 5));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JTextField txtNombre = new JTextField((String) modeloTabla.getValueAt(fila, 1));
            JTextField txtCurp = new JTextField((String) modeloTabla.getValueAt(fila, 2));
            JTextField txtTelefono = new JTextField((String) modeloTabla.getValueAt(fila, 3));
            JTextField txtCorreo = new JTextField((String) modeloTabla.getValueAt(fila, 4));

            panelFormulario.add(new JLabel("Nombre:"));
            panelFormulario.add(txtNombre);
            panelFormulario.add(new JLabel("Curp:"));
            panelFormulario.add(txtCurp);
            panelFormulario.add(new JLabel("Teléfono:"));
            panelFormulario.add(txtTelefono);
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

                try {
                    PacienteDTO pacienteActualizado = new PacienteDTO(idPaciente, nombre, curp, telefono, correo);
                    service.actualizarPaciente(pacienteActualizado);
                    JOptionPane.showMessageDialog(dialog, "Paciente actualizado correctamente.");
                    dialog.dispose();
                    cargarPacientes();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error RMI al actualizar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error inesperado al actualizar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            btnCancelarEditar.addActionListener(ev2 -> dialog.dispose());

            dialog.setVisible(true);
        });

        btnEliminar.addActionListener(ev -> {
            int fila = tablaPacientes.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un paciente para eliminar.");
                return;
            }

            int idPaciente = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.eliminarPaciente(idPaciente);
                    JOptionPane.showMessageDialog(this, "Paciente eliminado correctamente.");
                    cargarPacientes();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Error RMI al eliminar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error inesperado al eliminar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void cargarPacientes() {
        modeloTabla.setRowCount(0);
        try {
            List<PacienteDTO> pacientes = service.obtenerPacientes();
            if (pacientes != null) {
                for (PacienteDTO paciente : pacientes) {
                    modeloTabla.addRow(new Object[]{
                        paciente.getIdPaciente(),
                        paciente.getNombre(),
                        paciente.getCurp(),
                        paciente.getTelefono(),
                        paciente.getCorreo()
                    });
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error RMI al cargar pacientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado al cargar pacientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}