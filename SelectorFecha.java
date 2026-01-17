package vuelosfis.vista;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;

public class SelectorFecha extends JDialog {
    private LocalDate fechaActual;
    private JTextField txtReferencia; 
    private JLabel lblMesAnio;
    private JPanel panelDias;

    public SelectorFecha(Frame parent, JTextField txtReferencia) {
        super(parent, "Seleccionar Fecha", true);
        this.txtReferencia = txtReferencia;
        this.fechaActual = LocalDate.now();
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(new Color(0, 161, 222));
        
        JButton btnAnterior = new JButton("<");
        btnAnterior.addActionListener(e -> { fechaActual = fechaActual.minusMonths(1); actualizarCalendario(); });
        JButton btnSiguiente = new JButton(">");
        btnSiguiente.addActionListener(e -> { fechaActual = fechaActual.plusMonths(1); actualizarCalendario(); });

        lblMesAnio = new JLabel("", SwingConstants.CENTER);
        lblMesAnio.setForeground(Color.WHITE);
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 18));

        panelCabecera.add(btnAnterior, BorderLayout.WEST);
        panelCabecera.add(lblMesAnio, BorderLayout.CENTER);
        panelCabecera.add(btnSiguiente, BorderLayout.EAST);
        add(panelCabecera, BorderLayout.NORTH);

        panelDias = new JPanel(new GridLayout(0, 7, 5, 5));
        panelDias.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDias.setBackground(Color.WHITE);
        add(panelDias, BorderLayout.CENTER);
        
        actualizarCalendario();
    }

    private void actualizarCalendario() {
        panelDias.removeAll();
        lblMesAnio.setText(fechaActual.getMonth().name() + " " + fechaActual.getYear());
        String[] diasSemana = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
        for (String dia : diasSemana) panelDias.add(new JLabel(dia, SwingConstants.CENTER));

        YearMonth yearMonth = YearMonth.from(fechaActual);
        LocalDate primero = fechaActual.withDayOfMonth(1);
        int startDay = primero.getDayOfWeek().getValue();

        for (int i = 1; i < startDay; i++) panelDias.add(new JLabel(""));
        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {
            int d = dia;
            JButton btn = new JButton(String.valueOf(dia));
            btn.setBackground(Color.WHITE);
            btn.addActionListener(e -> {
                txtReferencia.setText(LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), d).toString());
                dispose();
            });
            panelDias.add(btn);
        }
        panelDias.revalidate();
        panelDias.repaint();
    }
}