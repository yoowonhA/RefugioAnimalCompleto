package Interfaz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import modelo.Animal;
import modelo.Gato;
import modelo.Perro;
import negocio.GestorAnimales;

public class VentanaAnimales extends JFrame implements ActionListener {

    private GestorAnimales gestor;

    private JTextField txtNombre, txtRaza, txtPeso, txtEdad, txtHistorial, txtUltimaAtencion, txtIngreso, txtCodigo;
    private JComboBox<String> cmbEspecie, cmbSexo, cmbTamanio, cmbEnergia, cmbSalud;
    private JButton btnRegistrar, btnEvaluar, btnTodos, btnDisponibles;
    private JTextArea areaListado;

    // Constructor que recibe el gestor compartido desde la VentanaPrincipal
    public VentanaAnimales(GestorAnimales gestor) {
        this.gestor = gestor;

        setTitle("Gestión de Animales");
        setSize(800, 620);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        agregarEtiqueta("Nombre o alias:", 20, 20);
        txtNombre = new JTextField();
        txtNombre.setBounds(180, 20, 180, 25);
        add(txtNombre);

        agregarEtiqueta("Especie:", 20, 55);
        cmbEspecie = new JComboBox<>(new String[]{"Perro", "Gato"});
        cmbEspecie.setBounds(180, 55, 180, 25);
        add(cmbEspecie);

        agregarEtiqueta("Raza:", 20, 90);
        txtRaza = new JTextField();
        txtRaza.setBounds(180, 90, 180, 25);
        add(txtRaza);

        agregarEtiqueta("Sexo:", 20, 125);
        cmbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        cmbSexo.setBounds(180, 125, 180, 25);
        add(cmbSexo);

        agregarEtiqueta("Peso (kg):", 20, 160);
        txtPeso = new JTextField();
        txtPeso.setBounds(180, 160, 180, 25);
        add(txtPeso);

        agregarEtiqueta("Edad al ingreso (meses/años):", 20, 195);
        txtEdad = new JTextField();
        txtEdad.setBounds(180, 195, 180, 25);
        add(txtEdad);

        agregarEtiqueta("Tamaño:", 400, 20);
        cmbTamanio = new JComboBox<>(new String[]{"Pequeño", "Mediano", "Grande"});
        cmbTamanio.setBounds(580, 20, 180, 25);
        add(cmbTamanio);

        agregarEtiqueta("Nivel de energía:", 400, 55);
        cmbEnergia = new JComboBox<>(new String[]{"Bajo", "Medio", "Alto"});
        cmbEnergia.setBounds(580, 55, 180, 25);
        add(cmbEnergia);

        agregarEtiqueta("Estado de salud:", 400, 90);
        cmbSalud = new JComboBox<>(new String[]{"Sano", "Herida leve", "Desnutrido", "Enfermo", "Crítico"});
        cmbSalud.setBounds(580, 90, 180, 25);
        add(cmbSalud);

        agregarEtiqueta("Historial médico:", 400, 125);
        txtHistorial = new JTextField();
        txtHistorial.setBounds(580, 125, 180, 25);
        add(txtHistorial);

        agregarEtiqueta("Última atención:", 400, 160);
        txtUltimaAtencion = new JTextField();
        txtUltimaAtencion.setBounds(580, 160, 180, 25);
        add(txtUltimaAtencion);

        agregarEtiqueta("Fecha de ingreso:", 400, 195);
        agregarEtiqueta("Las fechas van como dd/MM/yyyy", 480, 215);
        txtIngreso = new JTextField();
        txtIngreso.setBounds(580, 195, 180, 25);
        add(txtIngreso);

        btnRegistrar = new JButton("Registrar animal");
        btnRegistrar.setBounds(20, 240, 170, 30);
        btnRegistrar.addActionListener(this);
        add(btnRegistrar);

        agregarEtiqueta("Código (ej. ANI-0001):", 220, 245);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(370, 245, 100, 25);
        add(txtCodigo);

        btnEvaluar = new JButton("Evaluar urgencia");
        btnEvaluar.setBounds(490, 240, 150, 30);
        btnEvaluar.addActionListener(this);
        add(btnEvaluar);

        btnTodos = new JButton("Ver todos");
        btnTodos.setBounds(20, 285, 170, 30);
        btnTodos.addActionListener(this);
        add(btnTodos);

        btnDisponibles = new JButton("Ver disponibles");
        btnDisponibles.setBounds(210, 285, 170, 30);
        btnDisponibles.addActionListener(this);
        add(btnDisponibles);

        // Área donde se muestran los animales (con salto de línea automático integrado)
        areaListado = new JTextArea();
        areaListado.setEditable(false);
        areaListado.setLineWrap(true);          // Salto de línea automático para que no se desborde
        areaListado.setWrapStyleWord(true);     // Respeta las palabras completas al bajar de línea

        JScrollPane scroll = new JScrollPane(areaListado);
        scroll.setBounds(20, 330, 740, 240);
        add(scroll);

        // Muestra la lista actual al abrir la ventana si ya había registros previos
        mostrarAnimales(gestor.getListaAnimales());
    }

    private void agregarEtiqueta(String texto, int x, int y) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, 200, 25);
        add(etiqueta);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            registrarAnimal();
        } else if (e.getSource() == btnEvaluar) {
            String codigoBuscado = txtCodigo.getText().trim();
            if (codigoBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese el código del animal para evaluar su urgencia.");
                return;
            }
            String resultado = gestor.evaluarUrgenciaAnimal(codigoBuscado);
            JOptionPane.showMessageDialog(this, resultado);
            mostrarAnimales(gestor.getListaAnimales()); // Actualiza la vista por si cambió de estado
        } else if (e.getSource() == btnTodos) {
            mostrarAnimales(gestor.getListaAnimales());
        } else if (e.getSource() == btnDisponibles) {
            mostrarAnimales(gestor.listarAnimalesDisponibles());
        }
    }

    private void registrarAnimal() {
        double peso = -1;
        int edadMeses = -1;
        try {
            peso = Double.parseDouble(txtPeso.getText().trim());

            // Conversión inteligente: si ingresan un número menor o igual a 15, se asume que son años y se pasa a meses.
            double valorEdad = Double.parseDouble(txtEdad.getText().trim());
            if (valorEdad <= 15 && valorEdad > 0) {
                edadMeses = (int) (valorEdad * 12);
            } else {
                edadMeses = (int) valorEdad;
            }
        } catch (NumberFormatException ex) {
            // Si hay error de formato, se mantiene en -1 para que el gestor valide el error
        }

        String especie = (String) cmbEspecie.getSelectedItem();
        String codigo = gestor.generarCodigo();
        String sexo = (String) cmbSexo.getSelectedItem();
        String tamanio = (String) cmbTamanio.getSelectedItem();
        String energia = (String) cmbEnergia.getSelectedItem();
        String salud = (String) cmbSalud.getSelectedItem();

        Animal nuevo;
        if (especie.equals("Gato")) {
            nuevo = new Gato(codigo, txtNombre.getText().trim(), txtRaza.getText().trim(), sexo, peso, edadMeses,
                    tamanio, energia, salud, txtHistorial.getText().trim(), txtIngreso.getText().trim());
        } else {
            nuevo = new Perro(codigo, txtNombre.getText().trim(), txtRaza.getText().trim(), sexo, peso, edadMeses,
                    tamanio, energia, salud, txtHistorial.getText().trim(), txtIngreso.getText().trim());
        }
        nuevo.setFechaUltimaAtencion(txtUltimaAtencion.getText().trim());

        String mensaje = gestor.registrarAnimal(nuevo);
        if (mensaje.startsWith("Error")) {
            JOptionPane.showMessageDialog(this, mensaje);
        } else {
            JOptionPane.showMessageDialog(this, mensaje + "\nCódigo asignado: " + nuevo.getCodigo());
            limpiarCampos();
            mostrarAnimales(gestor.getListaAnimales());
        }
    }

    private void mostrarAnimales(ArrayList<Animal> animales) {
        areaListado.setText("");
        if (animales.isEmpty()) {
            areaListado.setText("No hay animales para mostrar en esta lista.");
            return;
        }
        for (Animal a : animales) {
            areaListado.append(a.toString() + "\n----------------------------------------------------\n");
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRaza.setText("");
        txtPeso.setText("");
        txtEdad.setText("");
        txtHistorial.setText("");
        txtUltimaAtencion.setText("");
        txtIngreso.setText("");
        txtCodigo.setText("");
    }

    public static void main(String[] args) {
        GestorAnimales gestor = new GestorAnimales();
        VentanaAnimales ventana = new VentanaAnimales(gestor);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
}