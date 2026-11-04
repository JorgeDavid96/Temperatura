package controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

import modelos.RegistroTemperatura;
import servicios.TemperaturasServicio;

public class TemperaturasControlador {
    private final TemperaturasServicio servicio;

    public TemperaturasControlador(TemperaturasServicio servicio) {
        this.servicio = servicio;
    }

    public int cargarCsv(String ruta) throws IOException {
        return servicio.cargarCsv(ruta.trim());
    }

    /** Convierte la fecha del DateChooser (Calendar) a LocalDate. */
    public LocalDate fecha(Calendar calendario) {
        if (calendario == null) {
            throw new IllegalArgumentException("Debe seleccionar una fecha");
        }
        return LocalDate.of(
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH) + 1,
                calendario.get(Calendar.DAY_OF_MONTH));
    }

    public Map<String, Double> promediosPorCiudad(LocalDate inicial, LocalDate fin) {
        return servicio.promedioPorCiudad(inicial, fin);
    }

    public Optional<RegistroTemperatura> ciudadMasCalurosa(LocalDate fecha) {
        return servicio.ciudadMasCalurosa(fecha);
    }

    public Optional<RegistroTemperatura> ciudadMenosCalurosa(LocalDate fecha) {
        return servicio.ciudadMenosCalurosa(fecha);
    }
}


