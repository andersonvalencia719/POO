package com.tecnomovil.data.service;

import com.tecnomovil.data.model.RegistroTransporte;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GeneradorDatosService {

    public static List<RegistroTransporte> generarRegistros(int cantidad, long semilla) {
        Random random = new Random(semilla);
        List<String> rutas = List.of("R1", "R2", "R3", "R4", "R5", "R6");
        List<String> estaciones = List.of("Centro", "Norte", "Sur", "Universidad",
                "Hospital", "Terminal", "Museo", "Parque", "Industria", "Aeropuerto");
        LocalDateTime inicio = LocalDateTime.of(2026, 10, 18, 5, 0);

        return IntStream.range(0, cantidad).mapToObj(i -> {
            int usuario = 1 + random.nextInt(300);
            String ruta = rutas.get(random.nextInt(rutas.size()));
            String estacion = estaciones.get(random.nextInt(estaciones.size()));
            String accion = random.nextDouble() < 0.58 ? "entrada" : "salida";
            LocalDateTime tiempo = inicio.plusMinutes(random.nextInt(1080));
            return new RegistroTransporte(usuario, ruta, estacion, accion, tiempo);
        }).sorted(Comparator.comparing(RegistroTransporte::timestamp)).toList();
    }
}