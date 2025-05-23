package cliente.gui;

import cliente.validacionEntradas.ValidarCita;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import common.interfaz.ServicioCitasRMI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        // Botones arriba
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

        // Etiqueta arriba de la tabla
        JLabel lblCitasAgendadas = new JLabel("Citas agendadas");
        lblCitasAgendadas.setFont(new Font("Arial", Font.BOLD, 12));
        lblCitasAgendadas.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Modelo y tabla
        String[] columnas = {"ID", "Fecha", "Hora", "Motivo", "Paciente", "Médico"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaCitas = new JTable(modeloTabla);

        // Datos de prueba
        modeloTabla.addRow(new Object[]{"1", "01/06/2025", "10:00", "Consulta general", "Juan Pérez", "Dr. Juan Pérez"});
        modeloTabla.addRow(new Object[]{"2", "02/06/2025", "14:30", "Revisión pediátrica", "Ana Gómez", "Dra. Ana Gómez"});

        JScrollPane scrollPane = new JScrollPane(tablaCitas);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblCitasAgendadas, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Datos simulados para pacientes y médicos
        String[] pacientes = {"Juan Pérez", "Ana Gómez", "Luis Martínez"};
        String[] medicos = {"Dr. Juan Pérez", "Dra. Ana Gómez", "Dr. Carlos Ruiz"};

        // Listener para selección en tabla
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

        // Método auxiliar para crear diálogo (Nuevo o Editar)
        Action crearDialogoCita = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean esEdicion = e.getSource() == btnEditar;
                int filaEditar = tablaCitas.getSelectedRow();

                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(VentanaCitas.this),
                        esEdicion ? "Editar Cita" : "Nueva Cita", true);
                dialog.setSize(400, 350);
                dialog.setLocationRelativeTo(VentanaCitas.this);
                dialog.setLayout(new BorderLayout(10, 10));

                JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
                panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JComboBox<String> comboPaciente = new JComboBox<>(pacientes);
                JComboBox<String> comboMedico = new JComboBox<>(medicos);

                SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
                JSpinner spinnerFecha = new JSpinner(dateModel);
                JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
                spinnerFecha.setEditor(dateEditor);

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

                if (esEdicion && filaEditar != -1) {
                    // Cargar datos actuales en el formulario
                    comboPaciente.setSelectedItem(modeloTabla.getValueAt(filaEditar, 4));
                    comboMedico.setSelectedItem(modeloTabla.getValueAt(filaEditar, 5));
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date fecha = sdf.parse((String) modeloTabla.getValueAt(filaEditar, 1));
                        spinnerFecha.setValue(fecha);
                    } catch (Exception ex) {
                        spinnerFecha.setValue(new Date());
                    }
                    String horaStr = (String) modeloTabla.getValueAt(filaEditar, 2);
                    String[] partesHora = horaStr.split(":");
                    comboHora.setSelectedItem(Integer.parseInt(partesHora[0]));
                    comboMinutos.setSelectedItem(Integer.parseInt(partesHora[1]));
                    txtMotivo.setText((String) modeloTabla.getValueAt(filaEditar, 3));
                }

                JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnGuardar = new JButton(esEdicion ? "Guardar" : "Agregar");
                JButton btnCancelar = new JButton("Cancelar");

                panelBotones.add(btnGuardar);
                panelBotones.add(btnCancelar);

                dialog.add(panelFormulario, BorderLayout.CENTER);
                dialog.add(panelBotones, BorderLayout.SOUTH);

                btnGuardar.addActionListener(ev2 -> {
                    String paciente = (String) comboPaciente.getSelectedItem();
                    String medico = (String) comboMedico.getSelectedItem();
                    Date fecha = (Date) spinnerFecha.getValue();
                    int hora = (int) comboHora.getSelectedItem();
                    int minuto = (int) comboMinutos.getSelectedItem();
                    String motivo = txtMotivo.getText().trim();

                    if (!ValidarCita.validarCita(paciente, medico, fecha, hora, minuto, motivo)) {
                        JOptionPane.showMessageDialog(dialog, "Datos inválidos o incompletos para la cita.");
                        return;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaStr = sdf.format(fecha);
                    String horaStr = String.format("%02d:%02d", hora, minuto);

                    if (esEdicion && filaEditar != -1) {
                        // Actualizar fila
                        modeloTabla.setValueAt(fechaStr, filaEditar, 1);
                        modeloTabla.setValueAt(horaStr, filaEditar, 2);
                        modeloTabla.setValueAt(motivo, filaEditar, 3);
                        modeloTabla.setValueAt(paciente, filaEditar, 4);
                        modeloTabla.setValueAt(medico, filaEditar, 5);
                        JOptionPane.showMessageDialog(dialog, "Cita actualizada correctamente.");
                    } else {
                        // Nueva cita
                        int nuevoId = modeloTabla.getRowCount() + 1;
                        modeloTabla.addRow(new Object[]{nuevoId, fechaStr, horaStr, motivo, paciente, medico});
                        JOptionPane.showMessageDialog(dialog, "Cita agregada correctamente.");
                    }
                    dialog.dispose();
                });

                btnCancelar.addActionListener(ev2 -> dialog.dispose());

                dialog.setVisible(true);
            }
        };

        // Asignar acción a botones
        btnNuevaCita.addActionListener(crearDialogoCita);
        btnEditar.addActionListener(crearDialogoCita);

        // Acción botón Eliminar
        btnEliminar.addActionListener(ev -> {
            int fila = tablaCitas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una cita para eliminar.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modeloTabla.removeRow(fila);
            }
        });
    }
}
