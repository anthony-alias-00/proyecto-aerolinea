/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

/**
 *
 * @author Antho
 */
public abstract class Avion {
    protected String matricula;
    protected int capacidad;

    public Avion(String matricula, int capacidad) {
        this.matricula = matricula;
        this.capacidad = capacidad;
    }

    // Método abstracto: Las clases hijas ESTÁN OBLIGADAS a implementarlo
    public abstract String getConfiguracion();

    public String getMatricula() { return matricula; }
    public int getCapacidad() { return capacidad; }
}
