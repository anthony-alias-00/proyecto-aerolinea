/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class Aeropuerto {
    private String codigoIATA; // Ej: UIO, AMS
    private String ciudad;
    private String pais;

    public Aeropuerto(String codigoIATA, String ciudad, String pais) {
        this.codigoIATA = codigoIATA;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters y Setters
    public String getCodigoIATA() { return codigoIATA; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    
    @Override
    public String toString() {
        return ciudad + " (" + codigoIATA + ")";
    }
}

