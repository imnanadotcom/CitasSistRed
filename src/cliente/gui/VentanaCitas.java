package cliente.gui;

import cliente.validacionEntradas.ValidarCita;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import common.CitaDTO;
import common.PacienteDTO;
import common.MedicoDTO;
import common.interfaz.ServicioCitasRMI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VentanaCitas extends JPanel {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnEditar;
    private JButton btnEliminar;
    private ServicioCitasRMI service;

    public VentanaCitas(ServicioCitasRMI service) {
        
        this.service = service;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnNuevaCita = new JButton("Nueva cita");
        btnNuevaCita.setBackground(Color.DARK_GRAY);
        btnNuevaCita.setForeground(Color.WHITE);
        btnNuevaCita.setFocusPainted(false);
        btnNuevaCita.setPreferredSize(new Dimension(150, 30));

        btnEditar = new JButton("Editar");
        btnEditar.setEnabled(false);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.add(btnNuevaCita);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.NORTH);

        JLabel lblCitasAgendadas = new JLabel("Citas agendadas");
        lblCitasAgendadas.setFont(new Font("Arial", Font.BOLD, 12));
        lblCitasAgendadas.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] columnas = {"ID", "Fecha", "Hora", "Motivo", "Paciente", "Médico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCitas = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaCitas);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblCitasAgendadas, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        tablaCitas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean filaSeleccionada = tablaCitas.getSelectedRow() != -1;
                    btnEditar.setEnabled(filaSeleccionada);
                    btnEliminar.setEnabled(filaSeleccionada);
                }
            }
        });

        cargarCitas();

        btnNuevaCita.addActionListener((ActionEvent e) -> {
            mostrarDialogoCita(false, -1);
        });

        btnEditar.addActionListener(ev -> {
            int fila = tablaCitas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una cita para editar.");
                return;
            }
            mostrarDialogoCita(true, fila);
        });

        btnEliminar.addActionListener(ev -> {
            int fila = tablaCitas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una cita para eliminar.");
                return;
            }

            int idCita = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    service.eliminarCita(idCita);
                    JOptionPane.showMessageDialog(this, "Cita eliminada correctamente.");
                    cargarCitas();
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Error RMI al eliminar cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error inesperado al eliminar cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
    }

    private void mostrarDialogoCita(boolean esEdicion, int filaEditar) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                esEdicion ? "Editar Cita" : "Nueva Cita", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cargar pacientes y médicos desde el servicio
        JComboBox<PacienteDTO> comboPaciente = new JComboBox<>();
        JComboBox<MedicoDTO> comboMedico = new JComboBox<>();
        
        try {
            List<PacienteDTO> pacientes = service.obtenerPacientes();
            if (pacientes != null) {
                for (PacienteDTO paciente : pacientes) {
                    comboPaciente.addItem(paciente);
                }
            }
            
            List<MedicoDTO> medicos = service.obtenerMedicos();
            if (medicos != null) {
                for (MedicoDTO medico : medicos) {
                    comboMedico.addItem(medico);
                }
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(dialog, "Error al cargar pacientes y médicos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Configurar spinner de fecha
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinnerFecha = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
        spinnerFecha.setEditor(dateEditor);

        // Configurar combos de hora
        Integer[] horas = new Integer[24];
        for (int i = 0; i < 24; i++) horas[i] = i;
        JComboBox<Integer> comboHora = new JComboBox<>(horas);

        Integer[] minutos = {0, 10, 20, 30, 40, 50};
        JComboBox<Integer> comboMinutos = new JComboBox<>(minutos);

        JTextField txtMotivo = new JTextField();

        panelFormulario.add(new JLabel("Paciente:"));
        panelFormulario.add(comboPaciente);
        panelFormulario.add(new JLabel("Médico:"));
        panelFormulario.add(comboMedico);
        panelFormulario.add(new JLabel("Fecha:"));
        panelFormulario.add(spinnerFecha);
        panelFormulario.add(new JLabel("Hora:"));
        panelFormulario.add(comboHora);
        panelFormulario.add(new JLabel("Minutos:"));
        panelFormulario.add(comboMinutos);
        panelFormulario.add(new JLabel("Motivo:"));
        panelFormulario.add(txtMotivo);

        // Si es edición, cargar datos actuales
        if (esEdicion && filaEditar != -1) {
            try {
                // Buscar y seleccionar el paciente correcto
                String nombrePaciente = (String) modeloTabla.getValueAt(filaEditar, 4);
                for (int i = 0; i < comboPaciente.getItemCount(); i++) {
                    PacienteDTO p = comboPaciente.getItemAt(i);
                    if (p.getNombre().equals(nombrePaciente)) {
                        comboPaciente.setSelectedIndex(i);
                        break;
                    }
                }

                // Buscar y seleccionar el médico correcto
                String nombreMedico = (String) modeloTabla.getValueAt(filaEditar, 5);
                for (int i = 0; i < comboMedico.getItemCount(); i++) {
                    MedicoDTO m = comboMedico.getItemAt(i);
                    if (m.getNombre().equals(nombreMedico)) {
                        comboMedico.setSelectedIndex(i);
                        break;
                    }
                }

                // Configurar fecha
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse((String) modeloTabla.getValueAt(filaEditar, 1));
                spinnerFecha.setValue(fecha);

                // Configurar hora
                String horaStr = (String) modeloTabla.getValueAt(filaEditar, 2);
                String[] partesHora = horaStr.split(":");
                comboHora.setSelectedItem(Integer.parseInt(partesHora[0]));
                comboMinutos.setSelectedItem(Integer.parseInt(partesHora[1]));

                txtMotivo.setText((String) modeloTabla.getValueAt(filaEditar, 3));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error al cargar datos de la cita: " + ex.getMessage());
            }
        }

        JPanel panelBotonesDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton(esEdicion ? "Guardar" : "Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotonesDialog.add(btnGuardar);
        panelBotonesDialog.add(btnCancelar);

        dialog.add(panelFormulario, BorderLayout.CENTER);
        dialog.add(panelBotonesDialog, BorderLayout.SOUTH);

        btnGuardar.addActionListener(ev2 -> {
            PacienteDTO pacienteSeleccionado = (PacienteDTO) comboPaciente.getSelectedItem();
            MedicoDTO medicoSeleccionado = (MedicoDTO) comboMedico.getSelectedItem();
            Date fecha = (Date) spinnerFecha.getValue();
            int hora = (int) comboHora.getSelectedItem();
            int minuto = (int) comboMinutos.getSelectedItem();
            String motivo = txtMotivo.getText().trim();

            if (pacienteSeleccionado == null || medicoSeleccionado == null) {
                JOptionPane.showMessageDialog(dialog, "Debe seleccionar un paciente y un médico.");
                return;
            }

            String horaFormateada = String.format("%02d:%02d", hora, minuto);

            if (!ValidarCita.validarCita(pacienteSeleccionado.getNombre(), medicoSeleccionado.getNombre(), fecha, hora, minuto, motivo)) {
                JOptionPane.showMessageDialog(dialog, "Datos inválidos o incompletos para la cita.");
                return;
            }

            try {
                if (esEdicion && filaEditar != -1) {
                    // Actualizar cita existente
                    int idCita = Integer.parseInt(modeloTabla.getValueAt(filaEditar, 0).toString());
                    CitaDTO citaActualizada = new CitaDTO(idCita, fecha, horaFormateada, motivo, 
                        pacienteSeleccionado.getIdPaciente(), medicoSeleccionado.getIdMedico(),
                        pacienteSeleccionado.getNombre(), medicoSeleccionado.getNombre());
                    
                    service.actualizarCita(citaActualizada);
                    JOptionPane.showMessageDialog(dialog, "Cita actualizada correctamente.");
                } else {
                    // Nueva cita
                    CitaDTO nuevaCita = new CitaDTO(0, fecha, horaFormateada, motivo,
                        pacienteSeleccionado.getIdPaciente(), medicoSeleccionado.getIdMedico(),
                        pacienteSeleccionado.getNombre(), medicoSeleccionado.getNombre());
                    
                    service.agregarCita(nuevaCita);
                    JOptionPane.showMessageDialog(dialog, "Cita agregada correctamente.");
                }
                
                dialog.dispose();
                cargarCitas();
                
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(dialog, "Error RMI al " + (esEdicion ? "actualizar" : "agregar") + " cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error inesperado al " + (esEdicion ? "actualizar" : "agregar") + " cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(ev2 -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void cargarCitas() {
        modeloTabla.setRowCount(0);
        try {
            List<CitaDTO> citas = service.obtenerCitas();
            if (citas != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (CitaDTO cita : citas) {
                    modeloTabla.addRow(new Object[]{
                        cita.getIdCita(),
                        sdf.format(cita.getFecha()),
                        cita.getHora(),
                        cita.getMotivo(),
                        cita.getNombrePaciente(),
                        cita.getNombreMedico()
                    });
                }
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error RMI al cargar citas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado al cargar citas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}