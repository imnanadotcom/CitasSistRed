package cliente.gui;

import javax.swing.*;

import Servidor.interfaz.ServicioCitasRMI;

import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

    private JPanel panelPrincipal;
    private CardLayout cardLayout;
    private ServicioCitasRMI service;

    public VentanaPrincipal(ServicioCitasRMI service) {

        this.service = service;

        setTitle("Sistema de citas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        JPanel panelLateral = new JPanel();
        panelLateral.setBackground(Color.LIGHT_GRAY);
        panelLateral.setPreferredSize(new Dimension(150, 0));
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Sistema de citas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelLateral.add(lblTitulo);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnPacientes = new JButton("Pacientes");
        JButton btnMedicos = new JButton("Médicos");
        JButton btnCitas = new JButton("Citas");

        Dimension btnSize = new Dimension(200, 40);
        btnPacientes.setMaximumSize(btnSize);
        btnMedicos.setMaximumSize(btnSize);
        btnCitas.setMaximumSize(btnSize);

        panelLateral.add(btnPacientes);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelLateral.add(btnMedicos);
        panelLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelLateral.add(btnCitas);

        add(panelLateral, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);

        JPanel panelMensaje = new JPanel(new BorderLayout());
        JLabel lblMensaje = new JLabel("Selecciona una sección");
        lblMensaje.setFont(new Font("Arial", Font.ITALIC, 16));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelMensaje.add(lblMensaje, BorderLayout.CENTER);

        JPanel panelPacientes = new VentanaPacientes(this.service);
        JPanel panelMedicos = new VentanaMedicos(this.service);
        JPanel panelCitas = new VentanaCitas(this.service);


        panelPrincipal.add(panelMensaje, "mensaje");
        panelPrincipal.add(panelPacientes, "pacientes");
        panelPrincipal.add(panelMedicos, "medicos");
        panelPrincipal.add(panelCitas, "citas");

        add(panelPrincipal, BorderLayout.CENTER);

        btnPacientes.addActionListener((ActionEvent e) -> cardLayout.show(panelPrincipal, "pacientes"));
        btnMedicos.addActionListener((ActionEvent e) -> cardLayout.show(panelPrincipal, "medicos"));
        btnCitas.addActionListener((ActionEvent e) -> cardLayout.show(panelPrincipal, "citas"));

        
        cardLayout.show(panelPrincipal, "mensaje");
    }


}
