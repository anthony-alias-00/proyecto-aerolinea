package vuelosfis.controlador;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import vuelosfis.modelo.*;
import vuelosfis.vista.*;

public class ControladorVuelos implements ActionListener {

    private VentanaPrincipal vista;
    private GestorArchivos modeloDatos;
    private Vuelo vueloSeleccionadoActual = null; 

    public ControladorVuelos(VentanaPrincipal vista) {
        this.vista = vista;
        this.modeloDatos = new GestorArchivos();
        
        this.vista.getBtnBuscar().addActionListener(this);
        this.vista.getBtnReservar().addActionListener(this);
        this.vista.getBtnCalIda().addActionListener(this);
        this.vista.getBtnCalRegreso().addActionListener(this);

        this.vista.getCmbTipoViaje().addActionListener(e -> {
            boolean esIdaVuelta = vista.getCmbTipoViaje().getSelectedItem().equals("Ida y Vuelta");
            vista.getTxtFechaRegreso().setEnabled(esIdaVuelta);
            vista.getBtnCalRegreso().setEnabled(esIdaVuelta);
            vista.getTxtFechaRegreso().setText(esIdaVuelta ? vista.getTxtFechaIda().getText() : "----");
        });

        this.vista.getCmbDestino().addActionListener(e -> actualizarNombreComida());
        actualizarNombreComida();
    }

    public void iniciar() { vista.setVisible(true); }

    private void actualizarNombreComida() {
        String seleccion = (String) vista.getCmbDestino().getSelectedItem();
        if (seleccion != null) {
            String codigo = seleccion.split(" - ")[0];
            vista.getChkComida().setText("Menu: " + obtenerPlatoTipico(codigo) + " (+$50)");
        }
    }

    private String obtenerPlatoTipico(String codigo) {
        switch (codigo) {
            case "UIO": return "Ceviche"; case "AMS": return "Stroopwafel"; case "MAD": return "Paella";
            case "JFK": return "Hamburguesa"; case "NRT": return "Sushi"; default: return "Internacional";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnBuscar()) buscarYListarVuelos();
        else if (e.getSource() == vista.getBtnReservar()) reservarVuelo();
        else if (e.getSource() == vista.getBtnCalIda()) new SelectorFecha(vista, vista.getTxtFechaIda()).setVisible(true);
        else if (e.getSource() == vista.getBtnCalRegreso()) new SelectorFecha(vista, vista.getTxtFechaRegreso()).setVisible(true);
    }

    private void buscarYListarVuelos() {
        JPanel panel = vista.getPanelResultados();
        panel.removeAll();
        vueloSeleccionadoActual = null;

        String codOrigen = ((String) vista.getCmbOrigen().getSelectedItem()).split(" - ")[0];
        String codDestino = ((String) vista.getCmbDestino().getSelectedItem()).split(" - ")[0];
        String fecha = vista.getTxtFechaIda().getText();

        if (codOrigen.equals(codDestino)) {
            DialogosKLM.mostrarMensaje(vista, "Error", "Origen y Destino no pueden ser iguales.", true);
            return;
        }

        boolean encontrado = false;
        for (Vuelo v : modeloDatos.leerVuelosDesdeArchivo()) {
            if (v.getRuta().getOrigen().getCodigoIATA().equals(codOrigen) &&
                v.getRuta().getDestino().getCodigoIATA().equals(codDestino) &&
                v.getFechaSalida().toLocalDate().toString().equals(fecha)) {
                
                String horaSalida = v.getFechaSalida().toString().split("T")[1];
                TarjetaVuelo tarjeta = new TarjetaVuelo(horaSalida, codOrigen, "08:00 (+1)", codDestino, "12h", String.valueOf(v.getPrecioBase()));
                
                tarjeta.getBtnSeleccionar().addActionListener(ev -> {
                    for (Component c : panel.getComponents()) if (c instanceof TarjetaVuelo) ((TarjetaVuelo)c).setSeleccionado(false);
                    tarjeta.setSeleccionado(true);
                    vueloSeleccionadoActual = v;
                });
                
                panel.add(tarjeta); panel.add(Box.createVerticalStrut(10));
                encontrado = true;
            }
        }
        panel.revalidate(); panel.repaint();
        if (!encontrado) DialogosKLM.mostrarMensaje(vista, "Sin Vuelos", "No hay vuelos para esta fecha.", true);
    }

    private void reservarVuelo() {
        if (vueloSeleccionadoActual == null) {
            DialogosKLM.mostrarMensaje(vista, "Atención", "Selecciona un vuelo primero.", true);
            return;
        }

        int pasajeros = (int) vista.getSpnPasajeros().getValue();
        int clase = DialogosKLM.mostrarSeleccionClase(vista, pasajeros);
        if (clase == -1) return;

        boolean esBusiness = (clase == 1);
        IEstrategiaPrecio estrategia = esBusiness ? new BusinessClass() : new EconomyLight();
        
        boolean quiereAsiento = DialogosKLM.mostrarPregunta(vista, "Asientos", "¿Elegir asientos por +$25?");
        String asientos = "";
        boolean pagoAsiento = false;

        if (quiereAsiento) {
            SelectorAsiento mapa = new SelectorAsiento(vista, esBusiness);
            mapa.setVisible(true);
            if (mapa.getAsientoSeleccionado() != null) {
                asientos = mapa.getAsientoSeleccionado() + (pasajeros > 1 ? " y vecinos" : "");
                pagoAsiento = true;
            } else asientos = generarAsientosMalvados(pasajeros, esBusiness);
        } else asientos = generarAsientosMalvados(pasajeros, esBusiness);

        double total = estrategia.calcularPrecio(vueloSeleccionadoActual.getPrecioBase());
        if (vista.getChkMaleta().isSelected()) total += 50;
        if (vista.getChkComida().isSelected()) total += 50;
        if (pagoAsiento) total += 25;
        
        boolean esIdaVuelta = vista.getCmbTipoViaje().getSelectedItem().equals("Ida y Vuelta");
        if (esIdaVuelta) total *= 2;
        total *= pasajeros;

        String desc = estrategia.getDescripcion() + (vista.getChkMaleta().isSelected()?"+Maleta ":"") + (pagoAsiento?"+Asiento ":"");
        
        if (DialogosKLM.mostrarConfirmacionPago(vista, vueloSeleccionadoActual.getCodigoVuelo(), esIdaVuelta?"Ida/Vuelta":"Ida", asientos, desc, String.format("%.2f", total))) {
            generarBoleto(vueloSeleccionadoActual.getCodigoVuelo(), asientos, total);
            DialogosKLM.mostrarMensaje(vista, "Éxito", "¡Reserva completada!", false);
        }
    }

    private String generarAsientosMalvados(int pax, boolean biz) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int min = biz?1:6, max=biz?5:30;
        for(int i=0; i<pax; i++) sb.append("[").append(r.nextInt(max-min)+min).append(r.nextBoolean()?"B":"E").append("] ");
        return sb.toString();
    }

    private void generarBoleto(String vuelo, String asientos, double total) {
        File f = new File("Boleto_" + System.currentTimeMillis() + ".txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println("KLM BOARDING PASS\nVuelo: " + vuelo + "\nAsientos: " + asientos + "\nTotal: $" + total);
        } catch (IOException e) {}
        try { Desktop.getDesktop().open(f); } catch (Exception e) {}
    }
}