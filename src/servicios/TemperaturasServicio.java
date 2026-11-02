package servicios;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modelos.RegistroTemperatura;

public class TemperaturasServicio {

    private List<RegistroTemperatura> datos;
    public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TemperaturasServicio() {
        this.datos = new ArrayList<>();
    }

    public int cargarCsv(String rutaArchivo) throws IOException {
        try (Stream<String> lineas = Files.lines(Paths.get(rutaArchivo), StandardCharsets.UTF_8)) {
            datos = lineas
                    .skip(1)
                    .filter(linea -> linea != null && !linea.isBlank())
                    .map(linea -> linea.split(","))
                    .filter(partes -> partes.length == 3)
                    .map(partes -> {
                        String ciudad = partes[0].trim();
                        LocalDate fecha = LocalDate.parse(partes[1].trim(), FORMATO_FECHA);
                        double temp = Double.parseDouble(partes[2].trim().replace(",", "."));
                        
                        return new RegistroTemperatura(ciudad, fecha, temp);
                    })
                    .collect(Collectors.toList());
        return datos.size();
        }
    }

    public Map<String, Double> promedioPorCiudad(LocalDate fechaInicial, LocalDate fechaFinal) {
        return datos.stream()
                .filter(r -> !r.getFecha().isBefore(fechaInicial) && !r.getFecha().isAfter(fechaFinal))
                .collect(Collectors.groupingBy(
                        RegistroTemperatura::getCiudad,
                        Collectors.averagingDouble(RegistroTemperatura::getTemperatura)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a, 
                        LinkedHashMap::new
                ));
    }

    public Optional<RegistroTemperatura> ciudadMasCalurosa(LocalDate fecha) {
        return datos.stream()
                .filter(r -> r.getFecha().equals(fecha))
                .max(Comparator.comparingDouble(RegistroTemperatura::getTemperatura)); 
    }

    public Optional<RegistroTemperatura> ciudadMenosCalurosa(LocalDate fecha) {
        return datos.stream()
                .filter(r -> r.getFecha().equals(fecha))
                .min(Comparator.comparingDouble(RegistroTemperatura::getTemperatura)); 
    }
}