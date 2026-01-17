/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class Ruta {
    private Aeropuerto origen;
    private Aeropuerto destino;
    private double duracionHoras;

    public Ruta(Aeropuerto origen, Aeropuerto destino, double duracionHoras) {
        this.origen = origen;
        this.destino = destino;
        this.duracionHoras = duracionHoras;
    }

    public Aeropuerto getOrigen() { return origen; }
    public Aeropuerto getDestino() { return destino; }
    public double getDuracionHoras() { return duracionHoras; }

    @Override
    public String toString() {
        return origen.getCodigoIATA() + " - " + destino.getCodigoIATA();
    }
}
