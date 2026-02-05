package vuelosfis.vista;

import javax.swing.*;
import javax.swing.border.*; // <--- ESTO ARREGLA EL ERROR (Importa LineBorder, MatteBorder, etc.)
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class FormularioPasajero extends JDialog {

    private JComboBox<String> cmbTipoDoc;
    private JTextField txtNombres, txtApellidos, txtNumDoc, txtTelefono, txtEmail, txtFechaNac;
    private JRadioButton rbtnTarjeta, rbtnPayPal;
    
    private JPanel panelDetallesTarjeta, panelPayPalInfo;
    private JTextField txtNumTarjeta, txtMesExp, txtAnioExp;
    private JPasswordField txtCvv;

    private boolean confirmado = false; 
    private final Color COLOR_AZUL_KLM = new Color(0, 161, 222);
    private final Color COLOR_FONDO = new Color(242, 246, 250);

    public FormularioPasajero(Frame parent) {
        super(parent, "Datos del Pasajero y Pago", true);
        
        // Configuración de ventana responsiva
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds(screenSize);
        setMinimumSize(new Dimension(800, 600));
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO);

        // HEADER
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 25));
        panelHeader.setBackground(Color.WHITE);
        JLabel lblTitulo = new JLabel("Datos del Pasajero");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(COLOR_AZUL_KLM);
        panelHeader.add(lblTitulo);
        add(panelHeader, BorderLayout.NORTH);

        // --- CONTENIDO PRINCIPAL ---
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_FONDO);
        panelContenido.setBorder(new EmptyBorder(20, 60, 20, 60)); // Márgenes laterales

        // SECCIÓN 1: DATOS PERSONALES (Sin Tratamiento, TitledBorder para que no se monte)
        JPanel panelDatos = new JPanel(new GridBagLayout());
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(crearBordeTitulo("Información Personal"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Espacio entre celdas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5; 

        // Fila 0: Nombres y Apellidos
        gbc.gridy = 0; gbc.gridx = 0;
        panelDatos.add(crearCampo("Nombres (Solo letras) *", txtNombres = new JTextField()), gbc);
        gbc.gridx = 1;
        panelDatos.add(crearCampo("Apellidos (Solo letras) *", txtApellidos = new JTextField()), gbc);

        // Fila 1: Documento
        gbc.gridy = 1; gbc.gridx = 0;
        panelDatos.add(crearCampo("Tipo Documento *", cmbTipoDoc = new JComboBox<>(new String[]{"Cédula (CI)", "Pasaporte"})), gbc);
        cmbTipoDoc.setBackground(Color.WHITE);
        gbc.gridx = 1;
        panelDatos.add(crearCampo("Número Documento *", txtNumDoc = new JTextField()), gbc);

        // Fila 2: Contacto
        gbc.gridy = 2; gbc.gridx = 0;
        panelDatos.add(crearCampo("Teléfono Móvil (Solo números) *", txtTelefono = new JTextField()), gbc);
        gbc.gridx = 1;
        panelDatos.add(crearCampo("Correo Electrónico (@) *", txtEmail = new JTextField()), gbc);
        
        // Fila 3: Fecha
        gbc.gridy = 3; gbc.gridx = 0;
        panelDatos.add(crearCampo("Fecha Nacimiento (DD/MM/AAAA) *", txtFechaNac = new JTextField()), gbc);
        // Relleno a la derecha
        gbc.gridx = 1; 
        panelDatos.add(Box.createGlue(), gbc);

        panelContenido.add(panelDatos);
        panelContenido.add(Box.createVerticalStrut(30)); 

        // SECCIÓN 2: PAGO
        JPanel panelPago = new JPanel();
        panelPago.setLayout(new BoxLayout(panelPago, BoxLayout.Y_AXIS));
        panelPago.setBackground(Color.WHITE);
        panelPago.setBorder(crearBordeTitulo("Pago Seguro"));

        // Radios
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        panelRadios.setBackground(Color.WHITE);
        rbtnTarjeta = new JRadioButton("Tarjeta de Crédito / Débito");
        rbtnTarjeta.setBackground(Color.WHITE); rbtnTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 15)); rbtnTarjeta.setSelected(true);
        rbtnPayPal = new JRadioButton("PayPal");
        rbtnPayPal.setBackground(Color.WHITE); rbtnPayPal.setFont(new Font("Segoe UI", Font.BOLD, 15));

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbtnTarjeta); grupo.add(rbtnPayPal);
        panelRadios.add(rbtnTarjeta); panelRadios.add(rbtnPayPal);
        panelPago.add(panelRadios);

        // --- PANEL TARJETA ---
        panelDetallesTarjeta = new JPanel(new GridBagLayout());
        panelDetallesTarjeta.setBackground(Color.WHITE);
        panelDetallesTarjeta.setBorder(new EmptyBorder(10, 15, 20, 15));

        txtNumTarjeta = new JTextField(); txtMesExp = new JTextField(3); txtAnioExp = new JTextField(3); txtCvv = new JPasswordField(4);
        
        // Número Tarjeta
        gbc.gridy = 0; gbc.gridx = 0; gbc.gridwidth = 2;
        panelDetallesTarjeta.add(crearCampo("Número de Tarjeta (Sin espacios) *", txtNumTarjeta), gbc);
        
        // Exp y CVV
        gbc.gridwidth = 1; gbc.gridy = 1; 
        
        gbc.gridx = 0;
        JPanel panelExp = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)); panelExp.setBackground(Color.WHITE);
        txtMesExp.setPreferredSize(new Dimension(50, 35)); txtAnioExp.setPreferredSize(new Dimension(50, 35));
        panelExp.add(txtMesExp); panelExp.add(new JLabel("/")); panelExp.add(txtAnioExp);
        JPanel pExpWrapper = new JPanel(new BorderLayout(0, 5)); pExpWrapper.setBackground(Color.WHITE);
        JLabel lExp = new JLabel("Exp (MM/AA) *"); lExp.setFont(new Font("Segoe UI", Font.PLAIN, 13)); lExp.setForeground(Color.GRAY);
        pExpWrapper.add(lExp, BorderLayout.NORTH); pExpWrapper.add(panelExp, BorderLayout.CENTER);
        panelDetallesTarjeta.add(pExpWrapper, gbc);
        
        gbc.gridx = 1;
        panelDetallesTarjeta.add(crearCampo("CVV (3-4 dígitos) *", txtCvv), gbc);
        
        // --- PANEL PAYPAL ---
        panelPayPalInfo = new JPanel(new BorderLayout());
        panelPayPalInfo.setBackground(new Color(245, 250, 255));
        panelPayPalInfo.setBorder(new EmptyBorder(30, 20, 30, 20)); 
        JLabel lblPP = new JLabel("<html><center><b>Será redirigido a PayPal para completar su pago.</b><br>No necesitamos tarjeta aquí.</center></html>", SwingConstants.CENTER);
        lblPP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPP.setForeground(new Color(0, 50, 150));
        panelPayPalInfo.add(lblPP, BorderLayout.CENTER);
        panelPayPalInfo.setVisible(false);

        panelPago.add(panelDetallesTarjeta);
        panelPago.add(panelPayPalInfo);
        panelContenido.add(panelPago);

        ActionListener listenerCambioPago = e -> {
            boolean esTarjeta = rbtnTarjeta.isSelected();
            panelDetallesTarjeta.setVisible(esTarjeta);
            panelPayPalInfo.setVisible(!esTarjeta);
            panelPago.revalidate(); panelPago.repaint();
        };
        rbtnTarjeta.addActionListener(listenerCambioPago);
        rbtnPayPal.addActionListener(listenerCambioPago);

        // --- SCROLLBAR ---
        JScrollPane scroll = new JScrollPane(panelContenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scroll, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));

        // FOOTER
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 60, 25));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.WHITE); btnCancelar.setBorder(null); btnCancelar.setForeground(Color.GRAY);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15)); btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(e -> dispose());

        JButton btnContinuar = new JButton("Confirmar y Pagar >");
        btnContinuar.setBackground(COLOR_AZUL_KLM); btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 18)); btnContinuar.setPreferredSize(new Dimension(250, 55));
        btnContinuar.setFocusPainted(false); btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinuar.addActionListener(e -> validarYContinuar());

        panelSur.add(btnCancelar); panelSur.add(btnContinuar);
        add(panelSur, BorderLayout.SOUTH);
    }

    // --- VALIDACIONES ---
    private void validarYContinuar() {
        if (!validarRegex(txtNombres.getText(), "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { alerta("El Nombre solo debe contener letras."); return; }
        if (!validarRegex(txtApellidos.getText(), "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) { alerta("El Apellido solo debe contener letras."); return; }

        String tipoDoc = (String) cmbTipoDoc.getSelectedItem();
        if (tipoDoc.contains("Cédula")) {
            if (!validarRegex(txtNumDoc.getText(), "^\\d{10}$")) { alerta("La Cédula debe tener exactamente 10 números."); return; }
        } else {
            if (!validarRegex(txtNumDoc.getText(), "^[a-zA-Z0-9]{5,15}$")) { alerta("El Pasaporte debe tener entre 5 y 15 caracteres."); return; }
        }

        if (!validarRegex(txtTelefono.getText(), "^\\d{7,15}$")) { alerta("El Teléfono debe ser numérico válido."); return; }
        if (!validarRegex(txtEmail.getText(), "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { alerta("Correo inválido."); return; }

        // Validación de fecha lógica
        if (!validarFechaLogica(txtFechaNac.getText())) {
            alerta("Fecha inválida. Use DD/MM/AAAA (Verifique días/meses correctos)."); 
            return;
        }

        if (rbtnTarjeta.isSelected()) {
            if (!validarRegex(txtNumTarjeta.getText(), "^\\d{13,19}$")) { alerta("Tarjeta inválida (13-19 dígitos)."); return; }
            if (!validarMes(txtMesExp.getText())) { alerta("Mes inválido (01-12)."); return; }
            if (!validarRegex(txtAnioExp.getText(), "^\\d{2}$")) { alerta("Año inválido (2 dígitos)."); return; }
            if (!validarRegex(new String(txtCvv.getPassword()), "^\\d{3,4}$")) { alerta("CVV inválido."); return; }
        }
        
        confirmado = true;
        dispose(); 
    }

    private boolean validarRegex(String texto, String regex) { return Pattern.matches(regex, texto.trim()); }
    
    private boolean validarFechaLogica(String fecha) {
        if (!Pattern.matches("^\\d{2}/\\d{2}/\\d{4}$", fecha.trim())) return false;
        try {
            String[] partes = fecha.trim().split("/");
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int anio = Integer.parseInt(partes[2]);
            if (mes < 1 || mes > 12) return false;
            if (dia < 1 || dia > 31) return false;
            if (anio < 1900 || anio > 2100) return false;
            if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) return false;
            if (mes == 2) {
                boolean bisiesto = (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
                if (dia > (bisiesto ? 29 : 28)) return false;
            }
            return true;
        } catch (Exception e) { return false; }
    }
    
    private boolean validarMes(String mes) {
        try { int m = Integer.parseInt(mes); return m >= 1 && m <= 12; } catch(Exception e) { return false; }
    }

    private void alerta(String msg) { DialogosKLM.mostrarMensaje(this, "Datos Incorrectos", msg, true); }

    public boolean isConfirmado() { return confirmado; }
    public String getNombreCompleto() { return txtNombres.getText() + " " + txtApellidos.getText(); }
    public String getDocumento() { return cmbTipoDoc.getSelectedItem() + ": " + txtNumDoc.getText(); }
    public String getMetodoPago() { 
        if (rbtnTarjeta.isSelected()) {
            String num = txtNumTarjeta.getText();
            String ultimos4 = (num.length() > 4) ? num.substring(num.length() - 4) : "xxxx";
            return "Tarjeta (**** " + ultimos4 + ")";
        } else return "PayPal (Verificado)";
    }

    // Helpers Visuales
    private TitledBorder crearBordeTitulo(String titulo) {
        TitledBorder borde = BorderFactory.createTitledBorder(new LineBorder(new Color(200, 200, 200), 1), titulo);
        borde.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        borde.setTitleColor(new Color(50, 50, 50));
        return borde;
    }
    
    private JPanel crearCampo(String titulo, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 8)); p.setBackground(Color.WHITE);
        JLabel l = new JLabel(titulo); l.setFont(new Font("Segoe UI", Font.PLAIN, 13)); l.setForeground(Color.GRAY);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        if (campo instanceof JTextField) ((JTextField)campo).setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(10, 10, 10, 10)));
        p.add(l, BorderLayout.NORTH); p.add(campo, BorderLayout.CENTER);
        return p;
    }
}