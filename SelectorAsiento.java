package vuelosfis.vista;

import javax.swing.*;
import java.awt.*;

public class SelectorAsiento extends JDialog {

    private String asientoSeleccionado = null; 
    private JButton btnConfirmar;

    public SelectorAsiento(Frame parent, boolean esBusiness) {
        super(parent, "Mapa de Asientos", true);
        setSize(550, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Cabina Boeing 787 - " + (esBusiness ? "Business" : "Economy"), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelAvion = new JPanel(new GridLayout(0, 7, 8, 8)); 
        panelAvion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] letras = {"A", "B", "C", "", "D", "E", "F"};
        int totalFilas = 30; 
        
        for (int fila = 1; fila <= totalFilas; fila++) {
            for (int col = 0; col < 7; col++) {
                if (col == 3) {
                    panelAvion.add(new JLabel(String.valueOf(fila), SwingConstants.CENTER));
                } else {
                    String nombreAsiento = fila + letras[col];
                    JButton btnAsiento = new JButton(nombreAsiento);
                    btnAsiento.setFocusPainted(false);
                    btnAsiento.setFont(new Font("Arial", Font.PLAIN, 10));
                    
                    if (fila <= 5) { // Business
                        btnAsiento.setBackground(new Color(200, 220, 255)); 
                        if (!esBusiness) btnAsiento.setEnabled(false);
                    } else { // Economy
                        btnAsiento.setBackground(new Color(240, 240, 240)); 
                        if (esBusiness) btnAsiento.setEnabled(false);
                    }

                    btnAsiento.addActionListener(e -> {
                        resetearColores(panelAvion);
                        btnAsiento.setBackground(new Color(100, 200, 100)); // Verde
                        asientoSeleccionado = nombreAsiento;
                        btnConfirmar.setEnabled(true);
                    });
                    panelAvion.add(btnAsiento);
                }
            }
        }
        
        JScrollPane scroll = new JScrollPane(panelAvion);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        btnConfirmar = new JButton("Confirmar Asiento");
        btnConfirmar.setBackground(new Color(0, 50, 100));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setEnabled(false); 
        btnConfirmar.setPreferredSize(new Dimension(500, 60));
        btnConfirmar.addActionListener(e -> dispose()); 
        add(btnConfirmar, BorderLayout.SOUTH);
    }

    private void resetearColores(JPanel panel) {
        for (Component c : panel.getComponents()) {
            if (c instanceof JButton && c.isEnabled()) {
                JButton btn = (JButton) c;
                int fila = Integer.parseInt(btn.getText().replaceAll("[^0-9]", ""));
                btn.setBackground(fila <= 5 ? new Color(200, 220, 255) : new Color(240, 240, 240));
            }
        }
    }
    public String getAsientoSeleccionado() { return asientoSeleccionado; }
}