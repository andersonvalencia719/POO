package com.tecnomovil.data.model;

import java.time.LocalDateTime;

public record RegistroTransporte(int idUsuario, String ruta, 
    String estacion, String accion, LocalDateTime timestamp) {
    public RegistroTransporte {
        if (idUsuario < 1 || ruta == null || estacion == null || 
            accion == null || timestamp == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (!accion.equals("entrada") && !accion.equals("salida")) {
            throw new IllegalArgumentException("La acción debe ser entrada o salida.");
        }
    }
}