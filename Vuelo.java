/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
import java.time.LocalDateTime;

public class Vuelo {
    private String codigoVuelo;
    private Ruta ruta;
    private LocalDateTime fechaSalida;
    private Avion avionAsignado;
    private double precioBase;

    public Vuelo(String codigoVuelo, Ruta ruta, LocalDateTime fechaSalida, Avion avion, double precioBase) {
        this.codigoVuelo = codigoVuelo;
        this.ruta = ruta;
        this.fechaSalida = fechaSalida;
        this.avionAsignado = avion;
        this.precioBase = precioBase;
    }

    public String getCodigoVuelo() { return codigoVuelo; }
    public Ruta getRuta() { return ruta; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public Avion getAvionAsignado() { return avionAsignado; }
    public double getPrecioBase() { return precioBase; }

    @Override
    public String toString() {
        return codigoVuelo + " [" + ruta.toString() + "]";
    }
}
