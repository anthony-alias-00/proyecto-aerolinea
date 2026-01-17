package vuelosfis.modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivos {

    private static final String RUTA_CSV = "archivos_datos/vuelos.csv";

    public List<Vuelo> leerVuelosDesdeArchivo() {
        List<Vuelo> lista = new ArrayList<>();
        Avion avionDefault = new Boeing787("PH-BHA (KLM)"); 

        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                
                if (datos.length >= 5) {
                    String codigo = datos[0];      
                    String codOrigen = datos[1];   
                    String codDestino = datos[2];  
                    String fechaStr = datos[3];    
                    double precio = Double.parseDouble(datos[4]); 

                    Aeropuerto origen = new Aeropuerto(codOrigen, "Ciudad " + codOrigen, "Pais");
                    Aeropuerto destino = new Aeropuerto(codDestino, "Ciudad " + codDestino, "Pais");
                    
                    Ruta ruta = new Ruta(origen, destino, 12.5);
                    LocalDateTime fecha = LocalDateTime.parse(fechaStr);

                    Vuelo nuevoVuelo = new Vuelo(codigo, ruta, fecha, avionDefault, precio);
                    lista.add(nuevoVuelo);
                }
            }
        } catch (IOException e) {
            System.err.println("Error leyendo el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error procesando datos: " + e.getMessage());
        }
        
        return lista;
    }
}