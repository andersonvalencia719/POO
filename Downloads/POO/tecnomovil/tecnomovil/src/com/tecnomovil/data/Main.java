package com.tecnomovil.data;

import com.tecnomovil.data.model.RegistroTransporte;
import com.tecnomovil.data.service.AnalisisTransporteService;
import com.tecnomovil.data.service.GeneradorDatosService;
import com.tecnomovil.data.view.ConsolaReporteView;
import java.util.List;

public class Main {
    private static final int TOTAL_REGISTROS = 12000;
    private static final long SEMILLA = 20261018L;
    private static final long UMBRAL_CRITICO = 1150;

    public static void main(String[] args) {
        // Inicialización de servicios y vistas
        AnalisisTransporteService servicioAnalisis = new AnalisisTransporteService();
        ConsolaReporteView vista = new ConsolaReporteView();

        // 1. Obtención de datos
        List<RegistroTransporte> registros = GeneradorDatosService.generarRegistros(TOTAL_REGISTROS, SEMILLA);

        // 2. Ejecución de análisis y presentación
        vista.mostrarEncabezado(registros.size());
        vista.mostrarTopEstaciones(servicioAnalisis.afluenciaPorEstacion(registros));
        vista.mostrarHorasPico(servicioAnalisis.entradasPorHora(registros));
        vista.mostrarRutasMasUtilizadas(servicioAnalisis.rutasMasUtilizadas(registros));
        vista.mostrarPatronesUsuarios(servicioAnalisis.patronesPorUsuario(registros));
        vista.mostrarTiempoPromedio(servicioAnalisis.tiempoPromedioEntreEstaciones(registros));
        vista.mostrarEstadoRutas(servicioAnalisis.detectarSobrecarga
            (registros, UMBRAL_CRITICO), UMBRAL_CRITICO);
        vista.mostrarPie(registros.size());
    }
}
