package vuelosfis.vista;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.time.LocalDate;

public class VentanaPrincipal extends JFrame {

    private JComboBox<String> cmbTipoViaje, cmbOrigen, cmbDestino;
    private JTextField txtFechaIda, txtFechaRegreso;
    private JButton btnCalIda, btnCalRegreso, btnBuscar, btnReservar;
    private JSpinner spnPasajeros;
    private JCheckBox chkMaleta, chkComida;
    private JPanel panelResultados; 
    
    private final Color COLOR_AZUL_KLM = new Color(0, 161, 222);
    private final Color COLOR_FONDO_APP = new Color(242, 246, 250);

    public VentanaPrincipal() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("KLM Royal Dutch Airlines - Reserva");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO_APP);
    }

    private void inicializarComponentes() {
        // --- HEADER ---
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 15));
        panelHeader.setBackground(COLOR_AZUL_KLM);
        JLabel lblLogo = new JLabel("<html><span style='font-size:28px'>KLM</span><br><span style='font-size:12px; font-weight:normal'>Royal Dutch Airlines</span></html>");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        panelHeader.add(lblLogo);

        // --- BARRA DE BÚSQUEDA ---
        JPanel panelBusquedaWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelBusquedaWrapper.setBackground(COLOR_FONDO_APP);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 4, 4, new Color(220, 220, 220)),
            new EmptyBorder(10, 20, 10, 20)
        ));

        String[] ciudades = { 
        "UIO - Quito", 
        "AMS - Amsterdam", 
        "MAD - Madrid", 
        "JFK - New York", 
        "MIA - Miami", 
        "BOG - Bogota",
        "NRT - Tokio",    
        "SYD - Sydney",
        "LHR - Londres",     
        "CDG - Paris",        
        "FCO - Roma",         
        "EZE - Buenos Aires",
        "BER - Berlin"
    };
        
        cmbTipoViaje = new JComboBox<>(new String[]{"Solo Ida", "Ida y Vuelta"}); styleCombo(cmbTipoViaje);
        cmbOrigen = new JComboBox<>(ciudades); cmbOrigen.setSelectedItem("UIO - Quito"); styleCombo(cmbOrigen);
        cmbDestino = new JComboBox<>(ciudades); cmbDestino.setSelectedItem("AMS - Amsterdam"); styleCombo(cmbDestino);
        
        // --- CAMBIO: FECHA EDITABLE ---
        txtFechaIda = new JTextField(LocalDate.now().toString(), 10); 
        txtFechaIda.setEditable(true); // <--- ¡AHORA PUEDES ESCRIBIR!
        txtFechaIda.setBackground(Color.WHITE);
        txtFechaIda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnCalIda = new JButton("📅"); btnCalIda.setBackground(Color.WHITE); btnCalIda.setBorder(null); btnCalIda.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        txtFechaRegreso = new JTextField("----", 10); 
        txtFechaRegreso.setEditable(false); // Inicia bloqueado hasta que elijas 'Ida y Vuelta'
        txtFechaRegreso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        btnCalRegreso = new JButton("📅"); btnCalRegreso.setEnabled(false); btnCalRegreso.setBackground(Color.WHITE); btnCalRegreso.setBorder(null);
        
        spnPasajeros = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
        btnBuscar = new JButton("Buscar Vuelos >");
        btnBuscar.setBackground(COLOR_AZUL_KLM);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBuscar.setPreferredSize(new Dimension(160, 40));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBusqueda.add(crearLabel("Viaje")); panelBusqueda.add(cmbTipoViaje);
        panelBusqueda.add(crearLabel("Origen")); panelBusqueda.add(cmbOrigen);
        panelBusqueda.add(crearLabel("Destino")); panelBusqueda.add(cmbDestino);
        panelBusqueda.add(crearLabel("Ida")); panelBusqueda.add(txtFechaIda); panelBusqueda.add(btnCalIda);
        panelBusqueda.add(crearLabel("Vuelta")); panelBusqueda.add(txtFechaRegreso); panelBusqueda.add(btnCalRegreso);
        panelBusqueda.add(crearLabel("Pax")); panelBusqueda.add(spnPasajeros);
        panelBusqueda.add(Box.createHorizontalStrut(10));
        panelBusqueda.add(btnBuscar);

        panelBusquedaWrapper.add(panelBusqueda);

        // --- EXTRAS ---
        JPanel panelExtras = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelExtras.setBackground(COLOR_FONDO_APP);
        chkMaleta = new JCheckBox("Equipaje Facturado (+$50)"); chkMaleta.setBackground(COLOR_FONDO_APP);
        chkComida = new JCheckBox("Menú Gourmet (+$50)"); chkComida.setBackground(COLOR_FONDO_APP);
        panelExtras.add(chkMaleta); panelExtras.add(chkComida);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(panelHeader, BorderLayout.NORTH);
        JPanel subTop = new JPanel(new BorderLayout()); 
        subTop.add(panelBusquedaWrapper, BorderLayout.CENTER); 
        subTop.add(panelExtras, BorderLayout.SOUTH);
        panelNorte.add(subTop, BorderLayout.SOUTH);

        // --- RESULTADOS ---
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(COLOR_FONDO_APP);
        panelResultados.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JScrollPane scroll = new JScrollPane(panelResultados);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // --- FOOTER ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 20));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        btnReservar = new JButton("Continuar a Asientos y Pago >");
        btnReservar.setBackground(new Color(0, 120, 60)); 
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnReservar.setPreferredSize(new Dimension(320, 50));
        btnReservar.setFocusPainted(false);
        btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panelSur.add(btnReservar);

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);
    }
    
    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(Color.GRAY);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }
    private void styleCombo(JComboBox box) {
        box.setBackground(Color.WHITE);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    // Getters
    public JPanel getPanelResultados() { return panelResultados; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnReservar() { return btnReservar; }
    public JButton getBtnCalIda() { return btnCalIda; }
    public JButton getBtnCalRegreso() { return btnCalRegreso; }
    public JComboBox<String> getCmbTipoViaje() { return cmbTipoViaje; }
    public JComboBox<String> getCmbOrigen() { return cmbOrigen; }
    public JComboBox<String> getCmbDestino() { return cmbDestino; }
    public JTextField getTxtFechaIda() { return txtFechaIda; }
    public JTextField getTxtFechaRegreso() { return txtFechaRegreso; }
    public JSpinner getSpnPasajeros() { return spnPasajeros; }
    public JCheckBox getChkMaleta() { return chkMaleta; }
    public JCheckBox getChkComida() { return chkComida; }
}