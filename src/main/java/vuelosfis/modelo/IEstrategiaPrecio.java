/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public interface IEstrategiaPrecio {
    // Método que calcula el precio final basado en un precio base
    double calcularPrecio(double precioBase);
    
    // Método para saber qué beneficios trae
    String getDescripcion();
}
