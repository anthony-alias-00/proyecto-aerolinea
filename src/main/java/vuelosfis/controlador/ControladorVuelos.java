package vuelosfis.controlador;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.Box;
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

        // Lógica para habilitar/deshabilitar la fecha de regreso
        this.vista.getCmbTipoViaje().addActionListener(e -> {
            boolean esIdaVuelta = vista.getCmbTipoViaje().getSelectedItem().equals("Ida y Vuelta");
            vista.getTxtFechaRegreso().setEditable(esIdaVuelta);
            vista.getTxtFechaRegreso().setEnabled(esIdaVuelta);
            vista.getBtnCalRegreso().setEnabled(esIdaVuelta);
            
            // Si activa la vuelta, copiamos la fecha de ida por comodidad
            if(esIdaVuelta && vista.getTxtFechaRegreso().getText().equals("----")) {
                 vista.getTxtFechaRegreso().setText(vista.getTxtFechaIda().getText());
            }
        });
        
        // Actualizar nombre del menú según destino (Detalle estético)
        this.vista.getCmbDestino().addActionListener(e -> actualizarNombreComida());
        actualizarNombreComida();
    }

    public void iniciar() { vista.setVisible(true); }

    private void actualizarNombreComida() {
        String seleccion = (String) vista.getCmbDestino().getSelectedItem();
        if (seleccion != null) {
            String codigo = seleccion.split(" - ")[0];
            String plato = "Gourmet";
            switch (codigo) {
                case "AMS": plato = "Stroopwafel"; break;
                case "MAD": plato = "Tapas"; break;
                case "NRT": plato = "Sushi"; break;
                case "FCO": plato = "Pasta"; break;
                case "EZE": plato = "Asado"; break;
                case "GYE": plato = "Encebollado"; break;
            }
            vista.getChkComida().setText("Menú: " + plato + " (+$50)");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnBuscar()) buscarYListarVuelos();
        else if (e.getSource() == vista.getBtnReservar()) reservarVuelo();
        // Conexión con el calendario bonito
        else if (e.getSource() == vista.getBtnCalIda()) new SelectorFecha(vista, vista.getTxtFechaIda()).setVisible(true);
        else if (e.getSource() == vista.getBtnCalRegreso()) new SelectorFecha(vista, vista.getTxtFechaRegreso()).setVisible(true);
    }

    // --- 1. LÓGICA DE BÚSQUEDA ---
    private void buscarYListarVuelos() {
        try {
            JPanel panel = vista.getPanelResultados();
            panel.removeAll();
            vueloSeleccionadoActual = null;

            String codOrigen = ((String) vista.getCmbOrigen().getSelectedItem()).split(" - ")[0];
            String codDestino = ((String) vista.getCmbDestino().getSelectedItem()).split(" - ")[0];
            String fechaBuscada = vista.getTxtFechaIda().getText().trim(); 

            if (codOrigen.equals(codDestino)) {
                DialogosKLM.mostrarMensaje(vista, "Error", "Origen y Destino no pueden ser iguales.", true);
                return;
            }

            List<Vuelo> todosLosVuelos = modeloDatos.leerVuelosDesdeArchivo();
            boolean encontrado = false;

            for (Vuelo v : todosLosVuelos) {
                if (v.getRuta().getOrigen().getCodigoIATA().equals(codOrigen) &&
                    v.getRuta().getDestino().getCodigoIATA().equals(codDestino) &&
                    v.getFechaSalida().toLocalDate().toString().equals(fechaBuscada)) {
                    
                    String horaSalida = String.format("%02d:%02d", v.getFechaSalida().getHour(), v.getFechaSalida().getMinute());
                    LocalDateTime llegada = v.getFechaSalida().plusMinutes(v.getRuta().getDuracionMinutos());
                    String horaLlegada = String.format("%02d:%02d", llegada.getHour(), llegada.getMinute());
                    if (llegada.getDayOfMonth() != v.getFechaSalida().getDayOfMonth()) horaLlegada += " (+1)";

                    TarjetaVuelo tarjeta = new TarjetaVuelo(horaSalida, codOrigen, horaLlegada, codDestino, "Directo", String.valueOf(v.getPrecioBase()));
                    
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

            if (!encontrado) {
                 DialogosKLM.mostrarMensaje(vista, "Sin Vuelos", "No hay vuelos para esta fecha y ruta.\nIntenta cambiar la fecha.", true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            DialogosKLM.mostrarMensaje(vista, "Error", "Verifica la fecha (AAAA-MM-DD).", true);
        }
    }

    // --- 2. LÓGICA DE RESERVA (¡AQUÍ ESTABA EL PROBLEMA!) ---
    private void reservarVuelo() {
        if (vueloSeleccionadoActual == null) {
            DialogosKLM.mostrarMensaje(vista, "Atención", "Por favor selecciona un vuelo primero.", true);
            return;
        }

        int pasajeros = (int) vista.getSpnPasajeros().getValue();
        boolean esIdaVuelta = vista.getCmbTipoViaje().getSelectedItem().equals("Ida y Vuelta");
        
        // A) ELEGIR CLASE (Economy / Business)
        int clase = DialogosKLM.mostrarSeleccionClase(vista, pasajeros);
        if (clase == -1) return; // Canceló
        boolean esBusiness = (clase == 1);
        
        // B) SELECCIÓN DE ASIENTOS
        String asientosIda = "";
        String asientosVuelta = "";
        double costoAsientosTotal = 0;

        // --- VUELO DE IDA ---
        boolean quiereAsientoIda = DialogosKLM.mostrarPregunta(vista, "Asientos IDA", "¿Desea elegir asientos para la IDA? (+$25 c/u)");
        if (quiereAsientoIda) {
            SelectorAsiento mapaIda = new SelectorAsiento(vista, esBusiness, pasajeros);
            mapaIda.setTitle("IDA: Seleccione " + pasajeros + " asientos");
            mapaIda.setVisible(true);
            
            String seleccion = mapaIda.getAsientosFormateados();
            if (seleccion != null) {
                asientosIda = seleccion;
                costoAsientosTotal += (25.0 * pasajeros);
            } else asientosIda = generarAsientosMalvados(pasajeros, esBusiness);
        } else {
            asientosIda = generarAsientosMalvados(pasajeros, esBusiness);
        }

        // --- VUELO DE VUELTA ---
        if (esIdaVuelta) {
            boolean quiereAsientoVuelta = DialogosKLM.mostrarPregunta(vista, "Asientos VUELTA", "¿Desea elegir asientos para el REGRESO? (+$25 c/u)");
            if (quiereAsientoVuelta) {
                SelectorAsiento mapaVuelta = new SelectorAsiento(vista, esBusiness, pasajeros);
                mapaVuelta.setTitle("VUELTA: Seleccione " + pasajeros + " asientos");
                mapaVuelta.setVisible(true);
                
                String seleccion = mapaVuelta.getAsientosFormateados();
                if (seleccion != null) {
                    asientosVuelta = seleccion;
                    costoAsientosTotal += (25.0 * pasajeros);
                } else asientosVuelta = generarAsientosMalvados(pasajeros, esBusiness);
            } else {
                asientosVuelta = generarAsientosMalvados(pasajeros, esBusiness);
            }
        } else {
            asientosVuelta = "N/A";
        }

        // C) DATOS DEL PASAJERO
        FormularioPasajero form = new FormularioPasajero(vista);
        form.setVisible(true);
        if (!form.isConfirmado()) return;

        String nombrePax = form.getNombreCompleto();
        String docPax = form.getDocumento();
        String metodoPago = form.getMetodoPago();

        // D) CÁLCULOS FINALES
        double precioBase = vueloSeleccionadoActual.getPrecioBase();
        
        // Estrategia de precio (Business sube el precio)
        if (esBusiness) precioBase *= 1.5; 
        
        // Si es ida y vuelta, se duplica la base
        if (esIdaVuelta) precioBase *= 2;
        
        // Extras por persona
        double extras = 0;
        if (vista.getChkMaleta().isSelected()) extras += 50;
        if (vista.getChkComida().isSelected()) extras += 50;
        if (esIdaVuelta) extras *= 2; // Extras se cobran doble si es ida y vuelta

        double totalPorPasajero = precioBase + extras;
        double granTotal = (totalPorPasajero * pasajeros) + costoAsientosTotal;

        // Descripción para el recibo
        StringBuilder desc = new StringBuilder();
        desc.append(esBusiness ? "Business Class" : "Economy");
        desc.append(esIdaVuelta ? " (Ida y Vuelta)" : " (Solo Ida)");
        if (extras > 0) desc.append(" + Extras");
        if (costoAsientosTotal > 0) desc.append(" + Selección Asientos");

        // E) CONFIRMACIÓN Y BOLETO
        if (DialogosKLM.mostrarConfirmacionPago(vista, vueloSeleccionadoActual.getCodigoVuelo(), 
                esIdaVuelta?"Ida/Vuelta":"Ida", "IDA: "+asientosIda + (esIdaVuelta?" | VUELTA: "+asientosVuelta:""), 
                desc.toString(), String.format("%.2f", granTotal))) {
            
            generarBoleto(vueloSeleccionadoActual.getCodigoVuelo(), asientosIda, asientosVuelta, granTotal, nombrePax, docPax, metodoPago);
            DialogosKLM.mostrarMensaje(vista, "¡Buen Viaje!", "Reserva exitosa. Boleto generado.", false);
        }
    }

    private String generarAsientosMalvados(int pax, boolean biz) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int min = biz?1:6, max=biz?5:30;
        for(int i=0; i<pax; i++) sb.append("[").append(r.nextInt(max-min)+min).append(r.nextBoolean()?"A":"F").append("] ");
        return sb.toString();
    }

    private void generarBoleto(String vuelo, String asientosIda, String asientosVuelta, double total, String nombre, String doc, String pago) {
        File f = new File("Boleto_KLM_" + System.currentTimeMillis() + ".txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println("******************************************");
            pw.println("        KLM - TARJETA DE EMBARQUE         ");
            pw.println("******************************************");
            pw.println("PASAJERO:  " + nombre.toUpperCase());
            pw.println("DOCUMENTO: " + doc);
            pw.println("------------------------------------------");
            pw.println("VUELO:     " + vuelo);
            pw.println("ORIGEN:    " + vista.getCmbOrigen().getSelectedItem());
            pw.println("DESTINO:   " + vista.getCmbDestino().getSelectedItem());
            pw.println("FECHA IDA: " + vista.getTxtFechaIda().getText());
            if (!asientosVuelta.equals("N/A")) {
                pw.println("FECHA REG: " + vista.getTxtFechaRegreso().getText());
            }
            pw.println("------------------------------------------");
            pw.println("ASIENTOS IDA:    " + asientosIda);
            if (!asientosVuelta.equals("N/A")) {
                pw.println("ASIENTOS VUELTA: " + asientosVuelta);
            }
            pw.println("------------------------------------------");
            pw.println("PAGO:      " + pago);
            pw.println("TOTAL PAGADO: $" + String.format("%.2f", total));
            pw.println("******************************************");
            pw.println("    ¡Gracias por volar con nosotros!      ");
        } catch (IOException e) { e.printStackTrace(); }
        
        try { Desktop.getDesktop().open(f); } catch (Exception e) {}
    }
}