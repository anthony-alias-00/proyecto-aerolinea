/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public class BusinessClass implements IEstrategiaPrecio {

    @Override
    public double calcularPrecio(double precioBase) {
        // Cuesta 2.5 veces más que la base, más $50 de tasas VIP
        return (precioBase * 2.5) + 50.0;
    }

    @Override
    public String getDescripcion() {
        return "BUSINESS CLASS: 2 Maletas (32kg), Acceso VIP, Comida Gourmet.";
    }
}
