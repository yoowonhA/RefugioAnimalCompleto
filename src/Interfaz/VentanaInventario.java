package Interfaz;

import java.awt.*;
import javax.swing.*;
import modelo.Insumo;
import negocio.GestorInventario;

public class VentanaInventario extends JFrame {
    private GestorInventario gestor;

    private JTextField txtId;
    private JTextField txtNombre;
    private JComboBox<String> cmbTipo;
    private JTextField txtDonante;
    private JTextField txtCantidad;
    private JComboBox<String> cmbUnidad;
    private JTextField txtFechaCaducidad;
    private JTextArea areaResultados;

    public VentanaInventario(GestorInventario gestor) {
        this.gestor = gestor;
        inicializarComponentes();
    }

    public VentanaInventario() {
        this(new GestorInventario());
    }

    private void inicializarComponentes() {
        setTitle("Módulo 4 - Gestión de Insumos y Donaciones");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 12));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(" Datos del Insumo y Donación "));

        panelFormulario.add(new JLabel("ID de insumo:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre del producto:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Tipo de insumo:"));
        cmbTipo = new JComboBox<>(new String[]{"Alimento", "Medicina", "Limpieza", "Cobijas / Accesorios"});
        panelFormulario.add(cmbTipo);

        panelFormulario.add(new JLabel("Donante:"));
        txtDonante = new JTextField();
        panelFormulario.add(txtDonante);

        panelFormulario.add(new JLabel("Cantidad y unidad:"));
        JPanel panelCantidadUnidad = new JPanel(new BorderLayout(5, 0));
        txtCantidad = new JTextField();
        cmbUnidad = new JComboBox<>(new String[]{"unidades", "quintales", "kg", "libras", "sacos", "cajas", "litros", "frascos"});
        cmbUnidad.setEditable(false); // Cambiado a false para que obligatoriamente tome la selección del menú desplegable sin fallos
        panelCantidadUnidad.add(txtCantidad, BorderLayout.CENTER);
        panelCantidadUnidad.add(cmbUnidad, BorderLayout.EAST);
        panelFormulario.add(panelCantidadUnidad);

        panelFormulario.add(new JLabel("Fecha de caducidad (AAAA-MM-DD o N/A):"));
        txtFechaCaducidad = new JTextField("N/A");
        panelFormulario.add(txtFechaCaducidad);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnRegistrar = new JButton("Registrar Insumo");
        JButton btnListar = new JButton("Listar Inventario");
        JButton btnAlertas = new JButton("Verificar Alertas de Stock");

        btnRegistrar.setPreferredSize(new Dimension(150, 30));
        btnListar.setPreferredSize(new Dimension(140, 30));
        btnAlertas.setPreferredSize(new Dimension(180, 30));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnAlertas);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);

        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        scrollResultados.setPreferredSize(new Dimension(700, 200));
        scrollResultados.setBorder(BorderFactory.createTitledBorder(" Resultados y Reportes del Inventario "));

        panelPrincipal.add(scrollResultados, BorderLayout.SOUTH);

        add(panelPrincipal);

        btnRegistrar.addActionListener(e -> registrarInsumo());
        btnListar.addActionListener(e -> listarInventario());
        btnAlertas.addActionListener(e -> verificarAlertas());

        if (!gestor.getListaInsumos().isEmpty()) {
            listarInventario();
        }
    }

    private void registrarInsumo() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tipo = (String) cmbTipo.getSelectedItem();
            String donante = txtDonante.getText().trim();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            // Captura directa y segura de la unidad seleccionada en el JComboBox
            String unidad = (String) cmbUnidad.getSelectedItem();
            if (unidad == null) {
                unidad = "unidades";
            }

            String caducidad = txtFechaCaducidad.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || donante.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Complete todos los campos obligatorios.", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String fechaActual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String registroConUnidad = fechaActual + " [" + unidad + "]";

            Insumo nuevo = new Insumo(id, nombre, tipo, donante, cantidad, caducidad, registroConUnidad);
            String mensaje = gestor.registrarInsumo(nuevo);

            JOptionPane.showMessageDialog(this, mensaje);
            if (mensaje.startsWith("Insumo registrado")) {
                limpiarCampos();
                listarInventario();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese solo el número entero en la cantidad (ej. 2) y elija la unidad al lado.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarInventario() {
        StringBuilder textoLista = new StringBuilder("=== INVENTARIO ACTUAL DEL REFUGIO ===\n\n");
        if (gestor.getListaInsumos().isEmpty()) {
            textoLista.append("No hay insumos registrados en el inventario actualmente.");
        } else {
            for (int i = 0; i < gestor.getListaInsumos().size(); i++) {
                Insumo ins = gestor.getListaInsumos().get(i);

                String fechaOriginal = ins.getFechaRegistro();
                String unidadMedida = "unidades";
                String fechaLimpia = fechaOriginal;

                if (fechaOriginal != null && fechaOriginal.contains("[")) {
                    int inicio = fechaOriginal.indexOf("[");
                    int fin = fechaOriginal.indexOf("]");
                    unidadMedida = fechaOriginal.substring(inicio + 1, fin);
                    fechaLimpia = fechaOriginal.substring(0, inicio).trim();
                }

                textoLista.append("- ID: ").append(ins.getIdInsumo())
                        .append(" | Producto: ").append(ins.getNombre())
                        .append(" (").append(ins.getTipoInsumo()).append(")")
                        .append(" | Cantidad: ").append(ins.getCantidad()).append(" ").append(unidadMedida)
                        .append(" | Donante: ").append(ins.getDonante())
                        .append(" | Caducidad: ").append(ins.getFechaCaducidad())
                        .append(" | Registro: ").append(fechaLimpia).append("\n\n");
            }
        }
        areaResultados.setText(textoLista.toString());
    }

    private void verificarAlertas() {
        String reporte = gestor.verificarAlertasStock();
        areaResultados.setText("=== REPORTE DE ALERTAS ===\n\n" + reporte);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtDonante.setText("");
        txtCantidad.setText("");
        cmbUnidad.setSelectedIndex(0);
        txtFechaCaducidad.setText("N/A");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaInventario ventana = new VentanaInventario();
            ventana.setVisible(true);
        });
    }
}