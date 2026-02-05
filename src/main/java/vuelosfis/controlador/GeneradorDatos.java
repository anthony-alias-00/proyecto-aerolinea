package vuelosfis.controlador;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

public class GeneradorDatos {

    public static void main(String[] args) {
        String[] aeropuertos = {
            "AMS", "UIO", "BCN", "IAH", "ARN", 
            "LHR", "BRU", "SYD", "NRT", "BOS", 
            "EZE", "MUC", "TLV", "JFK", "MAD", "CDG"
        };

        String rutaArchivo = "archivos_datos/vuelos.csv";
        Random random = new Random();
        int contadorVuelos = 1000;
        
        LocalDate fechaHoy = LocalDate.now();

        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            System.out.println("⏳ Generando vuelos para todo el año (esto puede tardar unos segundos)...");

            // --- CAMBIO AQUÍ: Generamos 400 días para cubrir todo 2026 de sobra ---
            for (int i = 0; i < 400; i++) { 
                LocalDate fechaGenerada = fechaHoy.plusDays(i);
                
                for (String origen : aeropuertos) {
                    for (String destino : aeropuertos) {
                        if (!origen.equals(destino)) {
                            // 30% de probabilidad de que NO haya vuelo (para realismo)
                            if (random.nextDouble() > 0.2) { 
                                String codigoVuelo = "KL" + contadorVuelos++;
                                int hora = random.nextInt(24);
                                String horaStr = (hora < 10 ? "0" + hora : "" + hora) + ":00";
                                int precio = 100 + random.nextInt(1400);

                                writer.write(codigoVuelo + "," + 
                                             origen + "," + 
                                             destino + "," + 
                                             fechaGenerada + "T" + horaStr + "," + 
                                             precio + ".00\n");
                            }
                        }
                    }
                }
            }
            System.out.println("¡ÉXITO! Base de datos actualizada con miles de vuelos hasta el 2026. ✈️");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}