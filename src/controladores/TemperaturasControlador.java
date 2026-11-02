package controladores;

import java.io.IOException;
import java.time.LocalDate;
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

    public LocalDate fecha(String texto) {
        return LocalDate.parse(texto.trim(), TemperaturasServicio.FORMATO_FECHA);
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


