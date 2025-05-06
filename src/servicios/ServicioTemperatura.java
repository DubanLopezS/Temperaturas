package servicios;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entidades.Temperatura;

public class ServicioTemperatura {

    public static List<Temperatura> getDatos(String nombreArchivo) {

        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("d/M/yyyy");
        try {
            Stream<String> lineas = Files.lines(Paths.get(nombreArchivo));
            return lineas.skip(1)
                    .map(linea -> linea.split(","))
                    .map(textos -> new Temperatura(textos[0], LocalDate.parse(textos[1], formatoFecha),
                            Double.parseDouble(textos[2])))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            return Collections.emptyList();

        }
    }
    
    public static List<Temperatura> filtrarPorFecha(List<Temperatura> datos, LocalDate desde, LocalDate hasta) {
        return datos.stream()
                .filter(temp -> !temp.getFecha().isBefore(desde) && !temp.getFecha().isAfter(hasta))
                .collect(Collectors.toList());
    }

    public static List<Temperatura> filtrarPorFechaEspecifica(List<Temperatura> datos, LocalDate especifica) {
        return datos.stream()
                .filter(temp -> temp.getFecha().equals(especifica))
                .collect(Collectors.toList());
    }
    

    public static Optional<String> getCiudadMasCalurosa(List<Temperatura> datos) {
        if (datos.isEmpty()) {
            return Optional.empty();
        }
        return datos.stream()
        .reduce((tempMayor, tempActual) -> tempMayor.getTemperatura() > tempActual.getTemperatura() ? tempMayor : tempActual) // Encuentra la mayor temperatura
        .map(temp -> temp.getCiudad() + " con " + temp.getTemperatura() + "°C"); 
    }

    public static Optional<String> getCiudadMasFria(List<Temperatura> datos) {
        if (datos.isEmpty()) {
            return Optional.empty();
        }
        return datos.stream()
            .reduce((minima, comparar) -> minima.getTemperatura() < comparar.getTemperatura() ? minima : comparar) // Encuentra la menor temperatura
            .map(temp -> temp.getCiudad() + " con " + temp.getTemperatura() + "°C"); 
    }


    public static Map<String, String> getCiudades(List<Temperatura> datos, LocalDate especifica) {

        var datosFiltrados = filtrarPorFechaEspecifica(datos, especifica); // Filtrar por fecha específica
    
        Map<String, String> ciudades = new LinkedHashMap<>();
        ciudades.put("Ciudad más calurosa:", getCiudadMasCalurosa(datosFiltrados).orElse("No disponible"));
        ciudades.put("Ciudad menos calurosa:", getCiudadMasFria(datosFiltrados).orElse("No disponible"));
    
        return ciudades;
    }
    

}
