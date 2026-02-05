package vuelosfis.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class SelectorFecha extends JDialog {

    private LocalDate fechaActual;
    private JTextField campoTextoDestino;
    private JPanel panelDias;
    private JLabel lblMesAnio;

    public SelectorFecha(Frame parent, JTextField campoTexto) {
        super(parent, "Seleccionar Fecha", true);
        this.campoTextoDestino = campoTexto;
        
        // Intentamos leer la fecha actual de la caja, si falla usamos HOY
        try {
            this.fechaActual = LocalDate.parse(campoTexto.getText());
        } catch (Exception e) {
            this.fechaActual = LocalDate.now();
        }

        setSize(350, 300); // Tamaño compacto
        setLocationRelativeTo(campoTexto);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header Azul
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(0, 161, 222));
        panelHeader.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnPrev = new JButton("<"); estiloBtn(btnPrev);
        btnPrev.addActionListener(e -> { fechaActual = fechaActual.minusMonths(1); actualizar(); });

        JButton btnNext = new JButton(">"); estiloBtn(btnNext);
        btnNext.addActionListener(e -> { fechaActual = fechaActual.plusMonths(1); actualizar(); });

        lblMesAnio = new JLabel("", SwingConstants.CENTER);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMesAnio.setForeground(Color.WHITE);

        panelHeader.add(btnPrev, BorderLayout.WEST);
        panelHeader.add(lblMesAnio, BorderLayout.CENTER);
        panelHeader.add(btnNext, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // Dias semana
        JPanel panelSemana = new JPanel(new GridLayout(1, 7));
        panelSemana.setBackground(Color.WHITE);
        for (String d : new String[]{"L", "M", "X", "J", "V", "S", "D"}) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(Color.GRAY);
            panelSemana.add(lbl);
        }
        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelSemana, BorderLayout.NORTH);
        
        // Grilla
        panelDias = new JPanel(new GridLayout(0, 7, 2, 2));
        panelDias.setBackground(Color.WHITE);
        panelDias.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelCentral.add(panelDias, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        actualizar();
    }

    private void actualizar() {
        panelDias.removeAll();
        lblMesAnio.setText(fechaActual.format(DateTimeFormatter.ofPattern("MMMM yyyy")).toUpperCase());

        YearMonth ym = YearMonth.from(fechaActual);
        LocalDate primero = fechaActual.withDayOfMonth(1);
        int diaSemana = primero.getDayOfWeek().getValue(); 

        for (int i = 1; i < diaSemana; i++) panelDias.add(new JLabel(""));

        for (int dia = 1; dia <= ym.lengthOfMonth(); dia++) {
            int d = dia;
            JButton btn = new JButton(String.valueOf(dia));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            if (LocalDate.now().getMonth() == fechaActual.getMonth() && LocalDate.now().getDayOfMonth() == dia) {
                btn.setForeground(new Color(0, 161, 222));
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            }
            btn.addActionListener(e -> {
                campoTextoDestino.setText(fechaActual.withDayOfMonth(d).toString());
                dispose();
            });
            panelDias.add(btn);
        }
        panelDias.revalidate(); panelDias.repaint();
    }

    private void estiloBtn(JButton b) {
        b.setBackground(new Color(0, 130, 190));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        b.setFocusPainted(false);
    }
}