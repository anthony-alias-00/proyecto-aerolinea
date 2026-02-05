package vuelosfis.modelo;

public class Ruta {
    private Aeropuerto origen;
    private Aeropuerto destino;
    private int duracionMinutos;

    // CONSTRUCTOR LIMPIO
    public Ruta(Aeropuerto origen, Aeropuerto destino, int duracionMinutos) {
        this.origen = origen;
        this.destino = destino;
        this.duracionMinutos = duracionMinutos;
    }

    public Aeropuerto getOrigen() { return origen; }
    public Aeropuerto getDestino() { return destino; }
    public int getDuracionMinutos() { return duracionMinutos; }
}
