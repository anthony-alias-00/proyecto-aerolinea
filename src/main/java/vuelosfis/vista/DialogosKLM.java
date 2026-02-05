package vuelosfis.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class DialogosKLM {

    private static final Color COLOR_AZUL_KLM = new Color(0, 161, 222);
    private static final Color COLOR_AZUL_OSCURO = new Color(0, 50, 100); // Azul Business
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

    // MENSAJE SIMPLE
    public static void mostrarMensaje(Component parent, String titulo, String mensaje, boolean esError) {
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), titulo, true);
        dialogo.setSize(450, 220);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());
        ((JComponent)dialogo.getContentPane()).setBorder(new LineBorder(COLOR_AZUL_KLM, 1));

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel lblIcono = new JLabel(esError ? "⚠️" : "✈️");
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel lblMsg = new JLabel("<html><body style='width: 250px'>" + mensaje + "</body></html>");
        lblMsg.setFont(FUENTE_NORMAL);

        panel.add(lblIcono, BorderLayout.WEST);
        panel.add(lblMsg, BorderLayout.CENTER);

        JButton btnOk = crearBotonAzul("Entendido");
        btnOk.addActionListener(e -> dialogo.dispose());
        
        JPanel panelSur = new JPanel();
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelSur.add(btnOk);

        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelSur, BorderLayout.SOUTH);
        dialogo.setUndecorated(true);
        dialogo.setVisible(true);
    }

    // SELECCIÓN DE CLASE (ESTILO PREMIUM vs ESTÁNDAR)
    public static int mostrarSeleccionClase(Component parent, int pasajeros) {
        final int[] seleccion = {-1}; 

        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Selección de Clase", true);
        dialogo.setSize(700, 450);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());
        ((JComponent)dialogo.getContentPane()).setBorder(new LineBorder(COLOR_AZUL_KLM, 2));

        JLabel lblTitulo = new JLabel("Elija su experiencia de viaje", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(new EmptyBorder(25, 0, 10, 0));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(Color.WHITE);
        dialogo.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelOpciones = new JPanel(new GridLayout(1, 2, 30, 0));
        panelOpciones.setBackground(Color.WHITE);
        panelOpciones.setBorder(new EmptyBorder(20, 40, 40, 40));

        // BOTÓN ECONOMY (Limpio)
        JButton btnEconomy = new JButton("<html><center><h2 style='color:#555'>ECONOMY</h2><br>Sin equipaje extra<br>Asiento estándar<br>Snack ligero<br><br><b style='color:#555'>Tarifa Base</b></center></html>");
        estilizarBotonOpcion(btnEconomy, false);
        btnEconomy.addActionListener(e -> { seleccion[0] = 0; dialogo.dispose(); });

        // BOTÓN BUSINESS (Lujo Oscuro)
        JButton btnBusiness = new JButton("<html><center><h2 style='color:white'>BUSINESS CLASS</h2><br><span style='color:#E0E0E0'>★ 2 Maletas (32kg)<br>★ Acceso VIP Lounge<br>★ Comida Gourmet<br>★ Prioridad Embarque</span><br><br><b style='color:#FFA500'>Tarifa Premium</b></center></html>");
        estilizarBotonOpcion(btnBusiness, true);
        btnBusiness.addActionListener(e -> { seleccion[0] = 1; dialogo.dispose(); });

        panelOpciones.add(btnEconomy);
        panelOpciones.add(btnBusiness);
        dialogo.add(panelOpciones, BorderLayout.CENTER);
        
        JButton btnCancelar = new JButton("Cancelar Reserva");
        btnCancelar.setForeground(Color.GRAY);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.addActionListener(e -> dialogo.dispose());
        dialogo.add(btnCancelar, BorderLayout.SOUTH);

        dialogo.setUndecorated(true);
        dialogo.setVisible(true);
        return seleccion[0];
    }

    // CONFIRMACIÓN DE PAGO (Estilo Recibo Limpio)
    public static boolean mostrarConfirmacionPago(Component parent, String vuelo, String tipo, String asientos, String detalle, String total) {
        final boolean[] confirmado = {false};

        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Pago", true);
        dialogo.setSize(500, 600);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());
        ((JComponent)dialogo.getContentPane()).setBorder(new LineBorder(COLOR_AZUL_KLM, 3));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setBorder(new EmptyBorder(40, 50, 40, 50));

        JLabel lblLogo = new JLabel("KLM Resumen de Compra");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(COLOR_AZUL_KLM);
        
        panelContenido.add(lblLogo);
        panelContenido.add(Box.createVerticalStrut(30));
        panelContenido.add(crearLineaDetalle("Vuelo:", vuelo));
        panelContenido.add(crearLineaDetalle("Tipo:", tipo));
        panelContenido.add(crearLineaDetalle("Asientos:", asientos));
        panelContenido.add(Box.createVerticalStrut(20));
        panelContenido.add(new JSeparator());
        panelContenido.add(Box.createVerticalStrut(20));
        
        JLabel lblDetalle = new JLabel("<html><b>Incluye:</b> " + detalle + "</html>");
        lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDetalle.setForeground(Color.GRAY);
        panelContenido.add(lblDetalle);
        
        panelContenido.add(Box.createVerticalStrut(40));
        JLabel lblTotal = new JLabel("Total: USD " + total);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotal.setForeground(COLOR_AZUL_OSCURO);
        panelContenido.add(lblTotal);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(new EmptyBorder(0, 30, 30, 30));

        JButton btnCancelar = new JButton("Volver");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setBorder(null);
        btnCancelar.addActionListener(e -> dialogo.dispose());

        JButton btnPagar = crearBotonAzul("Pagar y Emitir >");
        btnPagar.setPreferredSize(new Dimension(200, 50));
        btnPagar.addActionListener(e -> { confirmado[0] = true; dialogo.dispose(); });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnPagar);

        dialogo.add(panelContenido, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setUndecorated(true);
        dialogo.setVisible(true);
        return confirmado[0];
    }
    
    // PREGUNTA GENÉRICA
    public static boolean mostrarPregunta(Component parent, String titulo, String pregunta) {
        final boolean[] respuesta = {false};
        JDialog dialogo = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), titulo, true);
        dialogo.setSize(500, 250);
        dialogo.setLocationRelativeTo(parent);
        dialogo.setLayout(new BorderLayout());
        ((JComponent)dialogo.getContentPane()).setBorder(new LineBorder(COLOR_AZUL_KLM, 1));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JLabel lblPregunta = new JLabel("<html><h3>" + titulo + "</h3>" + pregunta + "</html>");
        lblPregunta.setFont(FUENTE_NORMAL);
        panel.add(lblPregunta, BorderLayout.CENTER);
        
        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtns.setBackground(Color.WHITE);
        panelBtns.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        JButton btnNo = new JButton("No, gracias");
        btnNo.setBackground(new Color(245, 245, 245));
        btnNo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnNo.setFocusPainted(false);
        btnNo.addActionListener(e -> dialogo.dispose());
        
        JButton btnSi = crearBotonAzul("Sí, añadir");
        btnSi.addActionListener(e -> { respuesta[0] = true; dialogo.dispose(); });
        
        panelBtns.add(btnNo);
        panelBtns.add(btnSi);
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(panelBtns, BorderLayout.SOUTH);
        dialogo.setUndecorated(true);
        dialogo.setVisible(true);
        return respuesta[0];
    }

    // --- UTILS ---
    private static JButton crearBotonAzul(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(COLOR_AZUL_KLM);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static void estilizarBotonOpcion(JButton btn, boolean esBusiness) {
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (esBusiness) {
            // LUJO: Azul oscuro
            btn.setBackground(COLOR_AZUL_OSCURO);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new MatteBorder(4, 4, 4, 4, COLOR_AZUL_OSCURO));
        } else {
            // ESTÁNDAR: Blanco
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(new MatteBorder(2, 2, 2, 2, new Color(220, 220, 220)));
        }
    }

    private static JPanel crearLineaDetalle(String label, String valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(1000, 30));
        p.add(new JLabel(label), BorderLayout.WEST);
        JLabel v = new JLabel(valor); v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        p.add(v, BorderLayout.EAST);
        return p;
    }
}