/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class Embraer190 extends Avion {

    public Embraer190(String matricula) {
        super(matricula, 100); // Para vuelos cortos (Cityhopper)
    }

    @Override
    public String getConfiguracion() {
        return "Embraer 190: 20 Business Europe, 8 Economy Comfort, 72 Economy";
    }
}
