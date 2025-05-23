package cliente.gui;

import cliente.validacionEntradas.ValidarMedico;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import common.MedicoDTO;
import common.interfaz.ServicioCitasRMI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.List;

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

        JLabel lblMedicosRegistrados = new JLabel("Médicos registrados");
        lblMedicosRegistrados.setFont(new Font("Arial", Font.BOLD, 12));
        lblMedicosRegistrados.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] columnas = {"ID", "Nombre", "Especialidad", "Cédula", "Correo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tablaMedicos = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaMedicos);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblMedicosRegistrados, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

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

        cargarMedicos();

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

                try {
                    MedicoDTO nuevoMedico = new MedicoDTO(0, nombre, especialidad, cedula, correo);
                    service.agregarMedico(nuevoMedico);
                    JOptionPane.showMessageDialog(dialog, "Médico agregado correctamente.");
                    dialog.dispose();
                    cargarMedicos();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error RMI al agregar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error inesperado al agregar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            btnCancelar.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        btnEditar.addActionListener(ev -> {
            int fila = tablaMedicos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un médico para editar.");
                return;
            }

            int idMedico = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

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

                try {
                    MedicoDTO medicoActualizado = new MedicoDTO(idMedico, nombre, especialidad, cedula, correo);
                    service.actualizarMedico(medicoActualizado);
                    JOptionPane.showMessageDialog(dialog, "Médico actualizado correctamente.");
                    dialog.dispose();
                    cargarMedicos();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(dialog, "Error RMI al actualizar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error inesperado al actualizar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            btnCancelarEditar.addActionListener(ev2 -> dialog.dispose());

            dialog.setVisible(true);
        });

        btnEliminar.addActionListener(ev -> {
            int fila = tablaMedicos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un médico para eliminar.");
                return;
            }

            int idMedico = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este médico?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.eliminarMedico(idMedico);
                    JOptionPane.showMessageDialog(this, "Médico eliminado correctamente.");
                    cargarMedicos();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Error RMI al eliminar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error inesperado al eliminar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void cargarMedicos() {
        modeloTabla.setRowCount(0);
        try {
            List<MedicoDTO> medicos = service.obtenerMedicos();
            if (medicos != null) {
                for (MedicoDTO medico : medicos) {
                    modeloTabla.addRow(new Object[]{
                        medico.getIdMedico(),
                        medico.getNombre(),
                        medico.getEspecialidad(),
                        medico.getCedula(),
                        medico.getCorreo()
                    });
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error RMI al cargar médicos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado al cargar médicos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}