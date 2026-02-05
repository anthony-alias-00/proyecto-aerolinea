package vuelosfis.vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectorAsiento extends JDialog {

    private List<String> asientosSeleccionados = new ArrayList<>();
    private int cantidadPasajeros;
    private JButton btnConfirmar;
    private JLabel lblContador;

    public SelectorAsiento(Frame parent, boolean esBusiness, int cantidadPasajeros) {
        super(parent, "Selección de Asientos", true);
        this.cantidadPasajeros = cantidadPasajeros;
        
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- CABECERA ---
        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        panelNorte.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("Seleccione sus asientos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        lblContador = new JLabel("Asientos seleccionados: 0 de " + cantidadPasajeros, SwingConstants.CENTER);
        lblContador.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContador.setForeground(new Color(0, 161, 222)); // Azul KLM

        panelNorte.add(lblTitulo);
        panelNorte.add(lblContador);
        add(panelNorte, BorderLayout.NORTH);

        // --- MAPA DE ASIENTOS ---
        JPanel panelAvion = new JPanel(new GridLayout(0, 7, 8, 8)); 
        panelAvion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] letras = {"A", "B", "C", "", "D", "E", "F"};
        int totalFilas = 30; 
        
        for (int fila = 1; fila <= totalFilas; fila++) {
            for (int col = 0; col < 7; col++) {
                if (col == 3) {
                    // Pasillo
                    panelAvion.add(new JLabel(String.valueOf(fila), SwingConstants.CENTER));
                } else {
                    String nombreAsiento = fila + letras[col];
                    JButton btnAsiento = new JButton(nombreAsiento);
                    btnAsiento.setFocusPainted(false);
                    btnAsiento.setFont(new Font("Arial", Font.PLAIN, 10));
                    
                    // Colores por clase
                    if (fila <= 5) { // Business
                        btnAsiento.setBackground(new Color(200, 220, 255)); 
                        if (!esBusiness) btnAsiento.setEnabled(false);
                    } else { // Economy
                        btnAsiento.setBackground(new Color(240, 240, 240)); 
                        if (esBusiness) btnAsiento.setEnabled(false);
                    }

                    // --- LÓGICA DE CLICK MULTIPLE ---
                    btnAsiento.addActionListener(e -> gestionarClickAsiento(btnAsiento, nombreAsiento));
                    
                    panelAvion.add(btnAsiento);
                }
            }
        }
        
        JScrollPane scroll = new JScrollPane(panelAvion);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // --- BOTÓN CONFIRMAR ---
        btnConfirmar = new JButton("Debe seleccionar " + cantidadPasajeros + " asientos");
        btnConfirmar.setBackground(Color.GRAY);
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setEnabled(false); // Desactivado al inicio
        btnConfirmar.setPreferredSize(new Dimension(500, 60));
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmar.addActionListener(e -> dispose()); 
        add(btnConfirmar, BorderLayout.SOUTH);
    }

    private void gestionarClickAsiento(JButton btn, String asiento) {
        if (asientosSeleccionados.contains(asiento)) {
            // DESELECCIONAR
            asientosSeleccionados.remove(asiento);
            // Restaurar color original
            int fila = Integer.parseInt(asiento.replaceAll("[^0-9]", ""));
            btn.setBackground(fila <= 5 ? new Color(200, 220, 255) : new Color(240, 240, 240));
        } else {
            // SELECCIONAR (Solo si no hemos llegado al límite)
            if (asientosSeleccionados.size() < cantidadPasajeros) {
                asientosSeleccionados.add(asiento);
                btn.setBackground(new Color(0, 161, 222)); // Azul Selección
                btn.setForeground(Color.WHITE); // Texto blanco
            } else {
                JOptionPane.showMessageDialog(this, "Ya seleccionaste los " + cantidadPasajeros + " asientos.");
            }
        }
        
        actualizarEstado();
    }

    private void actualizarEstado() {
        int actuales = asientosSeleccionados.size();
        lblContador.setText("Asientos seleccionados: " + actuales + " de " + cantidadPasajeros);
        
        if (actuales == cantidadPasajeros) {
            btnConfirmar.setEnabled(true);
            btnConfirmar.setBackground(new Color(0, 120, 60)); // Verde listo
            btnConfirmar.setText("Confirmar Selección");
        } else {
            btnConfirmar.setEnabled(false);
            btnConfirmar.setBackground(Color.GRAY);
            btnConfirmar.setText("Faltan " + (cantidadPasajeros - actuales) + " asientos");
        }
    }

    public String getAsientosFormateados() {
        if (asientosSeleccionados.size() < cantidadPasajeros) return null;
        
        // Devuelve string tipo "[1A] [1B] [1C]"
        StringBuilder sb = new StringBuilder();
        for (String s : asientosSeleccionados) {
            sb.append("[").append(s).append("] ");
        }
        return sb.toString();
    }
}