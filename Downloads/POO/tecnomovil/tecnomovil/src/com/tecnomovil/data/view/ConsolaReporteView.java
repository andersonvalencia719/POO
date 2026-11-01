package com.tecnomovil.data.view;

import com.tecnomovil.data.model.RutaEstado;
import com.tecnomovil.data.model.RutaUso;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConsolaReporteView {

    public void mostrarEncabezado(int totalRegistros) {
        System.out.println("=== TECNOMOVIL DATA: ANALISIS FUNCIONAL ===");
        System.out.println("Total de registros: " + totalRegistros);
    }

    public void mostrarTopEstaciones(Map<String, Long> afluencia) {
        System.out.println("\n1. Afluencia por estación (top 5):");
        afluencia.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue() + " entradas"));
    }

    public void mostrarHorasPico(Map<Integer, Long> entradasHora) {
        System.out.println("\n2. Horas de mayor flujo (top 5):");
        entradasHora.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.printf("%02d:00 - %d entradas%n", e.getKey(), e.getValue()));
    }

    public void mostrarRutasMasUtilizadas(List<RutaUso> rutas) {
        System.out.println("\n3. Rutas más utilizadas:");
        rutas.forEach(r -> System.out.println(r.ruta() + ": " + r.registros() + " eventos"));
    }

    public void mostrarPatronesUsuarios(Map<Integer, List<String>> patrones) {
        System.out.println("\n4. Patrones de viaje por usuario (primeros 3 usuarios, 8 estaciones):");
        patrones.entrySet().stream()
                .limit(3)
                .forEach(e -> System.out.println("Usuario " + e.getKey() + ": "
                        + e.getValue().stream().limit(8).collect(Collectors.joining(" -> "))));
    }

    public void mostrarTiempoPromedio(double promedioMinutos) {
        System.out.printf("%n5. Tiempo promedio entre estaciones diferentes: %.2f minutos%n", promedioMinutos);
    }

    public void mostrarEstadoRutas(List<RutaEstado> estados, long umbral) {
        System.out.println("\n6. Estado de las rutas (umbral: " + umbral + " entradas):");
        estados.forEach(r -> System.out.println(r.ruta() + ": " + r.entradas() + " entradas -> "
                + (r.critica() ? "CRITICA" : "Normal")));
    }

    public void mostrarPie(int totalRegistros) {
        System.out.println("\nValidación: la lista original conserva " + totalRegistros + " registros.");
        System.out.println("Ejecución finalizada correctamente.");
    }
}