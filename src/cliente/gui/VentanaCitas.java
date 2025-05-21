package cliente.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VentanaCitas extends JPanel {

    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;

    public VentanaCitas() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón Nueva cita
        JButton btnNuevaCita = new JButton("Nueva cita");
        btnNuevaCita.setBackground(Color.DARK_GRAY);
        btnNuevaCita.setForeground(Color.WHITE);
        btnNuevaCita.setFocusPainted(false);
        btnNuevaCita.setPreferredSize(new Dimension(150, 30));

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnNuevaCita);

        add(panelBoton, BorderLayout.NORTH);

        // Etiqueta citas agendadas
        JLabel lblCitasAgendadas = new JLabel("Citas agendadas");
        lblCitasAgendadas.setFont(new Font("Arial", Font.BOLD, 12));
        lblCitasAgendadas.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Modelo y tabla
        String[] columnas = {"ID", "Fecha", "Hora", "Motivo", "Paciente", "Médico"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaCitas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaCitas);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.add(lblCitasAgendadas, BorderLayout.NORTH);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // Datos simulados para pacientes y médicos
        String[] pacientes = {"Juan Pérez", "Ana Gómez", "Luis Martínez"};
        String[] medicos = {"Dr. Juan Pérez", "Dra. Ana Gómez", "Dr. Carlos Ruiz"};

        // Acción botón Nueva cita
        btnNuevaCita.addActionListener((ActionEvent e) -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva cita", true);
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout(10, 10));

            // Panel formulario con GridLayout
            JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
            panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Paciente
            panelFormulario.add(new JLabel("Paciente:"));
            JComboBox<String> comboPaciente = new JComboBox<>(pacientes);
            panelFormulario.add(comboPaciente);

            // Médico
            panelFormulario.add(new JLabel("Médico:"));
            JComboBox<String> comboMedico = new JComboBox<>(medicos);
            panelFormulario.add(comboMedico);

            // Fecha con JSpinner (selector de fecha)
            panelFormulario.add(new JLabel("Fecha:"));
            SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
            JSpinner spinnerFecha = new JSpinner(dateModel);
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
            spinnerFecha.setEditor(dateEditor);
            panelFormulario.add(spinnerFecha);

            // Hora
            panelFormulario.add(new JLabel("Hora:"));
            Integer[] horas = new Integer[24];
            for (int i = 0; i < 24; i++) horas[i] = i;
            JComboBox<Integer> comboHora = new JComboBox<>(horas);
            panelFormulario.add(comboHora);

            // Minutos (0, 10, 20, 30, 40, 50)
            panelFormulario.add(new JLabel("Minutos:"));
            Integer[] minutos = {0, 10, 20, 30, 40, 50};
            JComboBox<Integer> comboMinutos = new JComboBox<>(minutos);
            panelFormulario.add(comboMinutos);

            // Motivo
            panelFormulario.add(new JLabel("Motivo:"));
            JTextField txtMotivo = new JTextField();
            panelFormulario.add(txtMotivo);

            // Panel botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnAgregar = new JButton("Agregar");
            JButton btnCancelar = new JButton("Cancelar");
            panelBotones.add(btnAgregar);
            panelBotones.add(btnCancelar);

            dialog.add(panelFormulario, BorderLayout.CENTER);
            dialog.add(panelBotones, BorderLayout.SOUTH);

            btnAgregar.addActionListener(ev -> {
                String paciente = (String) comboPaciente.getSelectedItem();
                String medico = (String) comboMedico.getSelectedItem();
                Date fecha = (Date) spinnerFecha.getValue();
                Integer hora = (Integer) comboHora.getSelectedItem();
                Integer minuto = (Integer) comboMinutos.getSelectedItem();
                String motivo = txtMotivo.getText().trim();

                if (motivo.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "El motivo no puede estar vacío.");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String fechaStr = sdf.format(fecha);

                // Formatear hora y minuto para mostrar "HH:mm"
                String horaStr = String.format("%02d:%02d", hora, minuto);

                int nuevoId = modeloTabla.getRowCount() + 1;
                modeloTabla.addRow(new Object[]{nuevoId, fechaStr, horaStr, motivo, paciente, medico});

                JOptionPane.showMessageDialog(dialog, "Cita agregada correctamente.");
                dialog.dispose();
            });

            btnCancelar.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });
    }
}
