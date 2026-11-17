package Interfaz;

import modelo.Adoptante;
import modelo.Animal;
import negocio.GestorAdoptantes;
import negocio.GestorAnimales;
import negocio.GestorSolicitudes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class VentanaSolicitudes extends JFrame {
    private GestorSolicitudes gestor;
    private GestorAdoptantes gestorAdoptantes;
    private GestorAnimales gestorAnimales;

    private JTextField txtId;
    private JTextField txtCedulaAdoptante;
    private JTextField txtCodigoAnimal;
    private JTextArea txtResultado;

    // Constructor principal que recibe los gestores compartidos desde VentanaPrincipal
    public VentanaSolicitudes(GestorSolicitudes gestor, GestorAdoptantes gestorAdoptantes, GestorAnimales gestorAnimales) {
        this.gestor = gestor;
        this.gestorAdoptantes = gestorAdoptantes;
        this.gestorAnimales = gestorAnimales;
        inicializarComponentes();
    }

    // Constructor por defecto para evitar errores si se llama sin parámetros
    public VentanaSolicitudes() {
        this(new GestorSolicitudes(), new GestorAdoptantes(), new GestorAnimales());
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Solicitudes de Adopción - Módulo 3");
        setSize(525, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Evita cerrar toda la app al cerrar esta ventana
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Módulo 3: Gestión de Solicitudes");
        lblTitulo.setBounds(150, 20, 250, 25);
        add(lblTitulo);

        JLabel lblId = new JLabel("ID Solicitud:");
        lblId.setBounds(50, 60, 140, 25);
        add(lblId);

        txtId = new JTextField("SOL-001");
        txtId.setBounds(200, 60, 230, 25);
        add(txtId);

        JLabel lblAdoptante = new JLabel("Cédula Adoptante:");
        lblAdoptante.setBounds(50, 100, 140, 25);
        add(lblAdoptante);

        txtCedulaAdoptante = new JTextField();
        txtCedulaAdoptante.setBounds(200, 100, 230, 25);
        add(txtCedulaAdoptante);

        JLabel lblAnimalAsignado = new JLabel("Código Animal:");
        lblAnimalAsignado.setBounds(50, 140, 140, 25);
        add(lblAnimalAsignado);

        txtCodigoAnimal = new JTextField();
        txtCodigoAnimal.setBounds(200, 140, 230, 25);
        add(txtCodigoAnimal);

        JButton btnRegistrar = new JButton("Registrar Solicitud");
        btnRegistrar.setBounds(140, 190, 240, 30);
        add(btnRegistrar);

        JButton btnEvaluar = new JButton("Procesar Evaluación");
        btnEvaluar.setBounds(140, 235, 240, 30);
        add(btnEvaluar);

        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBounds(50, 280, 410, 130);
        add(scrollPane);

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String cedula = txtCedulaAdoptante.getText().trim();
                String codigoAnimal = txtCodigoAnimal.getText().trim();

                if (id.isEmpty() || cedula.isEmpty() || codigoAnimal.isEmpty()) {
                    txtResultado.setText("Error: Todos los campos (ID, Cédula y Código de Animal) son obligatorios.");
                    return;
                }

                // Busca en las listas reales donde se guardaron en las otras ventanas
                Adoptante ad = gestorAdoptantes.buscarAdoptantePorCedula(cedula);
                if (ad == null) {
                    txtResultado.setText("Error: No se encontró ningún adoptante registrado con la cédula: " + cedula);
                    return;
                }

                Animal an = gestorAnimales.buscarAnimalPorCodigo(codigoAnimal);
                if (an == null) {
                    txtResultado.setText("Error: No se encontró ningún animal registrado con el código: " + codigoAnimal);
                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.crearSolicitud(ad, an, id, "17/11/2026");

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());
            }
        });

        btnEvaluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                if (id.isEmpty()) {
                    txtResultado.setText("Error: Ingrese el ID de la solicitud que desea evaluar.");
                    return;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                PrintStream old = System.out;
                System.setOut(ps);

                gestor.procesarEvaluacionAdopcion(id);

                System.out.flush();
                System.setOut(old);
                txtResultado.setText(baos.toString());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaSolicitudes().setVisible(true));
    }
}