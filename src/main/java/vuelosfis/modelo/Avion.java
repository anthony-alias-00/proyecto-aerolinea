package vuelosfis.modelo;

public class Avion {
    protected String modelo;
    protected int capacidad;

    public Avion(String modelo, int capacidad) {
        this.modelo = modelo;
        this.capacidad = capacidad;
    }

    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
}