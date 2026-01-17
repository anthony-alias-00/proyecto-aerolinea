/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class Boeing787 extends Avion {

    public Boeing787(String matricula) {
        super(matricula, 294); // Capacidad fija real del Dreamliner de KLM
    }

    @Override
    public String getConfiguracion() {
        return "Boeing 787 Dreamliner: 30 Business, 48 Economy Comfort, 216 Economy";
    }
}
