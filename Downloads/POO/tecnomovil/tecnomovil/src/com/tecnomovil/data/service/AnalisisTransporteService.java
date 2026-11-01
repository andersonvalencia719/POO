package com.tecnomovil.data.service;

import com.tecnomovil.data.model.RegistroTransporte;
import com.tecnomovil.data.model.RutaEstado;
import com.tecnomovil.data.model.RutaUso;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AnalisisTransporteService {

    private static final Predicate<RegistroTransporte> ES_ENTRADA = r -> r.accion().equals("entrada");

    private static final Function<RegistroTransporte, Integer> HORA_DEL_EVENTO =
            ((Function<RegistroTransporte, LocalDateTime>) RegistroTransporte::timestamp)
                    .andThen(LocalDateTime::getHour);

    public static <K> Map<K, Long> contarPor(List<RegistroTransporte> registros,
                                             Predicate<RegistroTransporte> filtro,
                                             Function<RegistroTransporte, K> clasificador) {
        return registros.stream()
                .filter(filtro)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(clasificador, Collectors.counting()),
                        Collections::unmodifiableMap));
    }

    public Map<String, Long> afluenciaPorEstacion(List<RegistroTransporte> registros) {
        return contarPor(registros, ES_ENTRADA, RegistroTransporte::estacion);
    }

    public Map<Integer, Long> entradasPorHora(List<RegistroTransporte> registros) {
        return contarPor(registros, ES_ENTRADA, HORA_DEL_EVENTO);
    }

    public List<RutaUso> rutasMasUtilizadas(List<RegistroTransporte> registros) {
        return contarPor(registros, r -> true, RegistroTransporte::ruta)
                .entrySet().stream()
                .map(e -> new RutaUso(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(RutaUso::registros).reversed())
                .toList();
    }

    public Map<Integer, List<String>> patronesPorUsuario(List<RegistroTransporte> registros) {
        return registros.stream()
                .sorted(Comparator.comparing(RegistroTransporte::timestamp))
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(RegistroTransporte::idUsuario, TreeMap::new,
                                Collectors.mapping(RegistroTransporte::estacion,
                                        Collectors.toUnmodifiableList())),
                        Collections::unmodifiableMap));
    }

    public double tiempoPromedioEntreEstaciones(List<RegistroTransporte> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(RegistroTransporte::idUsuario))
                .values().stream()
                .flatMap(lista -> {
                    List<RegistroTransporte> ordenados = lista.stream()
                            .sorted(Comparator.comparing(RegistroTransporte::timestamp))
                            .toList();
                    return IntStream.range(1, ordenados.size())
                            .filter(i -> !ordenados.get(i).estacion()
                                    .equals(ordenados.get(i - 1).estacion()))
                            .mapToObj(i -> Duration.between(ordenados.get(i - 1).timestamp(),
                                    ordenados.get(i).timestamp()));
                })
                .mapToLong(Duration::toSeconds)
                .average()
                .orElse(0.0) / 60.0;
    }

    public List<RutaEstado> detectarSobrecarga(List<RegistroTransporte> registros, long umbral) {
        return contarPor(registros, ES_ENTRADA, RegistroTransporte::ruta)
                .entrySet().stream()
                .map(e -> new RutaEstado(e.getKey(), e.getValue(), e.getValue() > umbral))
                .sorted(Comparator.comparingLong(RutaEstado::entradas).reversed())
                .toList();
    }
}