package vuelosfis.vista;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class TarjetaVuelo extends JPanel {

    private JButton btnSeleccionar;
    private boolean seleccionado = false;

    // COLORES
    private final Color COLOR_AZUL_KLM = new Color(0, 161, 222);
    private final Color COLOR_NARANJA_PRECIO = new Color(235, 100, 10); 
    private final Color COLOR_TEXTO_OSCURO = new Color(30, 50, 70);
    private final Color COLOR_SOMBRA = new Color(210, 215, 220); 
    private final Color COLOR_FONDO_SELECCION = new Color(235, 250, 255);

    public TarjetaVuelo(String horaSalida, String origen, String horaLlegada, String destino, String duracion, String precio) {
        setLayout(new BorderLayout(20, 0));
        setBackground(Color.WHITE);

        MatteBorder sombra = new MatteBorder(0, 0, 3, 3, COLOR_SOMBRA);
        EmptyBorder padding = new EmptyBorder(20, 25, 20, 25);
        setBorder(new CompoundBorder(sombra, padding));

        // --- CAMBIO CLAVE: DIMENSIONES ELÁSTICAS ---
        // Height fijo (130), pero Width flexible
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 130)); 
        setAlignmentX(Component.CENTER_ALIGNMENT); // Para que se centre si sobra espacio
        // -------------------------------------------

        // IZQUIERDA
        JPanel panelInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        panelInfo.setOpaque(false);
        JLabel lblHorario = new JLabel(horaSalida + "  ➔  " + horaLlegada);
        lblHorario.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHorario.setForeground(COLOR_TEXTO_OSCURO);
        
        String rutaHtml = "<html><span style='font-size:15px; font-weight:bold'>" + origen + " - " + destino + "</span> " +
                          "<span style='color:#8899A6; font-size:13px'>&nbsp;&nbsp;|&nbsp;&nbsp;Duración: " + duracion + "&nbsp;&nbsp;|&nbsp;&nbsp;Directo</span></html>";
        JLabel lblRuta = new JLabel(rutaHtml);
        lblRuta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelInfo.add(lblHorario); panelInfo.add(lblRuta);

        // DERECHA
        JPanel panelPrecio = new JPanel(new BorderLayout(0, 12));
        panelPrecio.setOpaque(false);
        JLabel lblPrecio = new JLabel("USD " + precio);
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblPrecio.setForeground(COLOR_NARANJA_PRECIO);
        lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);

        btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.setBackground(Color.WHITE);
        btnSeleccionar.setForeground(COLOR_AZUL_KLM);
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSeleccionar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(2, 2, 2, 2, COLOR_AZUL_KLM),
                new EmptyBorder(8, 25, 8, 25)
        ));
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelPrecio.add(lblPrecio, BorderLayout.NORTH);
        panelPrecio.add(btnSeleccionar, BorderLayout.SOUTH);

        add(panelInfo, BorderLayout.CENTER);
        add(panelPrecio, BorderLayout.EAST);
    }

    public JButton getBtnSeleccionar() { return btnSeleccionar; }

    public void setSeleccionado(boolean estado) {
        this.seleccionado = estado;
        if (estado) {
            setBackground(COLOR_FONDO_SELECCION);
            setBorder(new CompoundBorder(new MatteBorder(2, 2, 2, 2, COLOR_AZUL_KLM), new EmptyBorder(20, 25, 20, 25)));
            btnSeleccionar.setBackground(COLOR_AZUL_KLM);
            btnSeleccionar.setForeground(Color.WHITE);
            btnSeleccionar.setText("✓ Seleccionado");
        } else {
            setBackground(Color.WHITE);
            setBorder(new CompoundBorder(new MatteBorder(0, 0, 3, 3, COLOR_SOMBRA), new EmptyBorder(20, 25, 20, 25)));
            btnSeleccionar.setBackground(Color.WHITE);
            btnSeleccionar.setForeground(COLOR_AZUL_KLM);
            btnSeleccionar.setText("Seleccionar");
        }
    }
}