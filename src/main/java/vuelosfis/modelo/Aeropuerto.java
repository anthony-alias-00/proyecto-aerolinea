package vuelosfis.modelo;

public class Aeropuerto {
    private String codigoIATA;
    private String nombre;
    private String ciudad; 

    // CONSTRUCTOR LIMPIO (Sin throw new...)
    public Aeropuerto(String codigoIATA, String nombre, String ciudad) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getCodigoIATA() { return codigoIATA; }
    public String getNombre() { return nombre; }
    public String getCiudad() { return ciudad; }
    
    @Override
    public String toString() {
        return codigoIATA + " - " + ciudad;
    }
}