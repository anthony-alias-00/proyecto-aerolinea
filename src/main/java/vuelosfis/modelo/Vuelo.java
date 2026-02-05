package vuelosfis.modelo;

import java.time.LocalDateTime;

public class Vuelo {
    private String codigoVuelo;
    private Ruta ruta;
    private LocalDateTime fechaSalida;
    private Avion avion; 
    private double precioBase;

    public Vuelo(String codigoVuelo, Ruta ruta, LocalDateTime fechaSalida, Avion avion, double precioBase) {
        this.codigoVuelo = codigoVuelo;
        this.ruta = ruta;
        this.fechaSalida = fechaSalida;
        this.avion = avion;
        this.precioBase = precioBase;
    }

    public String getCodigoVuelo() { return codigoVuelo; }
    public Ruta getRuta() { return ruta; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public double getPrecioBase() { return precioBase; }
    
    // ESTE MÉTODO ES IMPORTANTE
    public Avion getAvion() { return this.avion; }
}