package Interfaz;

import negocio.GestorAdoptantes;
import negocio.GestorAnimales;
import negocio.GestorInventario;
import negocio.GestorSolicitudes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    // Instancias únicas y compartidas para toda la aplicación (evita que se borren los datos al cerrar ventanas)
    private GestorAnimales gestorAnimales = new GestorAnimales();
    private GestorAdoptantes gestorAdoptantes = new GestorAdoptantes();
    private GestorSolicitudes gestorSolicitudes = new GestorSolicitudes();
    private GestorInventario gestorInventario = new GestorInventario();

    private JButton btnAnimales, btnAdoptantes, btnSolicitudes, btnInventario;

    public VentanaPrincipal() {
        setTitle("Sistema de Refugio de Animales - Menú Principal");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Refugio de Animales");
        lblTitulo.setBounds(130, 20, 150, 25);
        add(lblTitulo);

        btnAnimales = new JButton("Gestión de Animales");
        btnAnimales.setBounds(90, 70, 200, 35);
        add(btnAnimales);

        btnAdoptantes = new JButton("Gestión de Adoptantes");
        btnAdoptantes.setBounds(90, 120, 200, 35);
        add(btnAdoptantes);

        btnSolicitudes = new JButton("Gestión de Solicitudes");
        btnSolicitudes.setBounds(90, 170, 200, 35);
        add(btnSolicitudes);

        btnInventario = new JButton("Gestión de Inventario");
        btnInventario.setBounds(90, 220, 200, 35);
        add(btnInventario);

        // --- ACCIONES DE LOS BOTONES (Pasando los gestores compartidos) ---

        btnAnimales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAnimales ventana = new VentanaAnimales(gestorAnimales);
                ventana.setVisible(true);
            }
        });

        btnAdoptantes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAdoptantes ventana = new VentanaAdoptantes(gestorAdoptantes);
                ventana.setVisible(true);
            }
        });

        btnSolicitudes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Se pasan los tres gestores para que la ventana de solicitudes pueda validar cédulas y códigos reales
                VentanaSolicitudes ventana = new VentanaSolicitudes(gestorSolicitudes, gestorAdoptantes, gestorAnimales);
                ventana.setVisible(true);
            }
        });

        btnInventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaInventario ventana = new VentanaInventario(gestorInventario);
                ventana.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}