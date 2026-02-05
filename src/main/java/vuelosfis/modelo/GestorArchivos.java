package vuelosfis.modelo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestorArchivos {

    private final String RUTA_ARCHIVO = System.getProperty("user.home") + File.separator + "vuelos_klm_db.txt";

    // TUS CIUDADES DEFINITIVAS
    private final String[] CODIGOS = {
        "UIO", "AMS", "MAD", "JFK", "MIA", "BOG", 
        "NRT", "SYD", "LHR", "CDG", "FCO", "EZE", 
        "BER", // Berlin
        "GYE", "CUE", "GPS" // Nacionales
    };
    
    private final String[] CIUDADES = {
        "Quito", "Amsterdam", "Madrid", "New York", "Miami", "Bogota", 
        "Tokio", "Sydney", "Londres", "Paris", "Roma", "Buenos Aires", 
        "Berlin",
        "Guayaquil", "Cuenca", "Galapagos"
    };

    public List<Vuelo> leerVuelosDesdeArchivo() {
        List<Vuelo> vuelos = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);

        // --- CAMBIO CLAVE: SOLO GENERAMOS SI EL ARCHIVO NO EXISTE ---
        // Si el archivo ya está ahí, NO tocamos nada. Respetamos los vuelos existentes.
        if (!archivo.exists()) {
            System.out.println(">>> Creando nueva base de datos de vuelos...");
            generarItinerarioMallaCompleta();
        } else {
            System.out.println(">>> Leyendo base de datos existente.");
        }
        // -----------------------------------------------------------

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    String[] datos = linea.split(",");
                    if (datos.length >= 6) {
                        Aeropuerto origen = new Aeropuerto(datos[1], "AP " + datos[1], buscarCiudad(datos[1]));
                        Aeropuerto destino = new Aeropuerto(datos[2], "AP " + datos[2], buscarCiudad(datos[2]));
                        Ruta ruta = new Ruta(origen, destino, calcularDuracion(datos[1], datos[2]));
                        
                        LocalDateTime fecha = LocalDateTime.parse(datos[3]);
                        double precio = Double.parseDouble(datos[4]);
                        Avion avion = new AvionComercial(datos[5], 250);
                        
                        vuelos.add(new Vuelo(datos[0], ruta, fecha, avion, precio));
                    }
                } catch (Exception e) { }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return vuelos;
    }

    private void generarItinerarioMallaCompleta() {
        List<Vuelo> listaVuelos = new ArrayList<>();
        Random rand = new Random();
        
        // Generamos vuelos para 6 meses (180 días)
        LocalDateTime fechaBase = LocalDateTime.now().minusDays(5).withHour(0).withMinute(0);

        for (int dia = 0; dia < 180; dia++) {
            LocalDateTime fechaDia = fechaBase.plusDays(dia);
            
            for (int i = 0; i < CODIGOS.length; i++) {
                for (int j = 0; j < CODIGOS.length; j++) {
                    if (i == j) continue; 
                    
                    String origen = CODIGOS[i];
                    String destino = CODIGOS[j];
                    
                    boolean esLargo = esVueloLargo(origen, destino);
                    // 1 vuelo diario para rutas largas, 2-4 para cortas
                    int cantidad = esLargo ? 1 : (2 + rand.nextInt(3)); 
                    
                    for (int k = 0; k < cantidad; k++) {
                        int hora = 5 + rand.nextInt(19); 
                        int minuto = rand.nextInt(60);
                        
                        LocalDateTime salida = fechaDia.withHour(hora).withMinute(minuto);
                        String codigo = "KL" + (100 + rand.nextInt(9000));
                        String avion = esLargo ? "Boeing 787-10" : "Boeing 737-MAX";
                        double precio = esLargo ? (1200 + rand.nextInt(500)) : (300 + rand.nextInt(200));
                        
                        Aeropuerto ao = new Aeropuerto(origen, "AP "+origen, CIUDADES[i]);
                        Aeropuerto ad = new Aeropuerto(destino, "AP "+destino, CIUDADES[j]);
                        
                        listaVuelos.add(new Vuelo(codigo, new Ruta(ao, ad, 0), salida, new AvionComercial(avion, 200), precio));
                    }
                }
            }
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_ARCHIVO))) {
            for (Vuelo v : listaVuelos) {
                pw.println(v.getCodigoVuelo() + "," + v.getRuta().getOrigen().getCodigoIATA() + "," +
                           v.getRuta().getDestino().getCodigoIATA() + "," + v.getFechaSalida() + "," + 
                           v.getPrecioBase() + "," + v.getAvion().getModelo());
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    private String buscarCiudad(String codigo) {
        for(int i=0; i<CODIGOS.length; i++) if(CODIGOS[i].equals(codigo)) return CIUDADES[i];
        return "Desconocida";
    }
    
    private boolean esVueloLargo(String o, String d) {
        String largos = "AMS/MAD/NRT/SYD/LHR/CDG/FCO/BER/EZE";
        return largos.contains(o) || largos.contains(d);
    }
    
    private int calcularDuracion(String o, String d) {
        if (esVueloLargo(o, d)) return 720 + new Random().nextInt(300);
        return 180 + new Random().nextInt(200);
    }
}