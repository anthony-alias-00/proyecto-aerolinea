/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class EconomyLight implements IEstrategiaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        // Es la tarifa base, sin recargos
        return precioBase;
    }

    @Override
    public String getDescripcion() {
        return "ECONOMY LIGHT: Solo equipaje de mano (8kg). Sin cambios.";
    }
}
