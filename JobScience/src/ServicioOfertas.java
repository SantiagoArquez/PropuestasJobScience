import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ServicioOfertas {

    private static final String NO_DISPONIBLE = "N/D";

    private List<OfertaEmpleo> ofertas;

    public ServicioOfertas(String archivo) throws IOException {

        Path ruta = Paths.get(archivo);

        ofertas = Files.lines(ruta)
                .skip(1)
                .map(ServicioOfertas::separarLinea)
                .filter(datos -> datos.length >= 13)
                .map(datos -> {
                    double[] salario = leerSalario(datos[11]);
                    return new OfertaEmpleo(
                            valor(datos[0]),
                            valor(datos[1]),
                            valor(datos[2]),
                            valor(datos[3]),
                            valor(datos[4]),
                            valor(datos[5]),
                            valor(datos[6]),
                            valor(datos[7]),
                            valor(datos[8]),
                            valor(datos[9]),
                            valor(datos[10]),
                            salario[0],
                            salario[1],
                            leerHabilidades(datos[12]));
                })
                .collect(Collectors.toList());
    }

    // ---------- Metodos auxiliares de lectura ----------

    // Separa una linea CSV respetando los campos entre comillas (que pueden traer comas)
    private static String[] separarLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean enComillas = false;

        for (char c : linea.toCharArray()) {
            if (c == '"') {
                enComillas = !enComillas;
            } else if (c == ',' && !enComillas) {
                campos.add(actual.toString().trim());
                actual.setLength(0);
            } else {
                actual.append(c);
            }
        }
        campos.add(actual.toString().trim());
        return campos.toArray(new String[0]);
    }

    // Los campos vacios se reemplazan por "N/D" (no disponible)
    private static String valor(String texto) {
        return texto == null || texto.isBlank() ? NO_DISPONIBLE : texto;
    }

    // Convierte "€100,472 - €200,938" o "€133,489" en {minimo, maximo}
    private static double[] leerSalario(String texto) {
        String limpio = texto.replace("€", "").replace(",", "").trim();
        String[] partes = limpio.split(" - ");
        double minimo = Double.parseDouble(partes[0].trim());
        double maximo = partes.length > 1 ? Double.parseDouble(partes[1].trim()) : minimo;
        return new double[] { minimo, maximo };
    }

    // Convierte "['python', 'sql']" en una lista de habilidades
    private static List<String> leerHabilidades(String texto) {
        String limpio = texto.replace("[", "").replace("]", "").replace("'", "").trim();
        if (limpio.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(limpio.split(","))
                .map(String::trim)
                .filter(h -> !h.isEmpty())
                .collect(Collectors.toList());
    }

    // 1. Total de registros cargados
    public void totalRegistros() {
        System.out.println("TOTAL REGISTROS: " + ofertas.size());
    }

    // 2. Primeros 10 registros
    public void mostrarPrimeros10() {
        System.out.println("PRIMEROS 10 REGISTROS");
        ofertas.stream()
                .limit(10)
                .forEach(System.out::println);
    }

    // 3. Ordenar por salario promedio descendente (Comparator)
    public void ordenarPorSalario() {
        System.out.println("OFERTAS ORDENADAS POR SALARIO (DESCENDENTE)");
        ofertas.stream()
                .sorted(Comparator.comparingDouble(OfertaEmpleo::getSalarioPromedio).reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    // 4. Ordenar por industria y, en caso de empate, por salario descendente (thenComparing)
    public void ordenarPorIndustriaYSalario() {
        System.out.println("OFERTAS ORDENADAS POR INDUSTRIA Y SALARIO");
        Comparator<OfertaEmpleo> porIndustria = Comparator.comparing(OfertaEmpleo::getIndustria);
        Comparator<OfertaEmpleo> porSalario =
                Comparator.comparingDouble(OfertaEmpleo::getSalarioPromedio).reversed();

        ofertas.stream()
                .sorted(porIndustria.thenComparing(porSalario))
                .limit(10)
                .forEach(System.out::println);
    }

    // 5. Mostrar cargo, empresa y salario usando Consumer
    public void ejemploConsumer() {
        System.out.println("CONSUMER");

        Consumer<OfertaEmpleo> consumidor =
                oferta -> System.out.println(
                        oferta.getTitulo() + " | " + oferta.getEmpresa()
                                + " -> EUR " + String.format(Locale.US, "%.2f", oferta.getSalarioPromedio()));

        ofertas.stream()
                .limit(10)
                .forEach(consumidor);
    }

    // 6. Filtrar ofertas con salario promedio mayor a 150000 usando Predicate
    public void ejemploPredicateSalario() {
        System.out.println("PREDICATE - SALARIO MAYOR A 150000");

        Predicate<OfertaEmpleo> salarioAlto = oferta -> oferta.getSalarioPromedio() > 150000;

        ofertas.stream()
                .filter(salarioAlto)
                .limit(10)
                .forEach(System.out::println);
    }

    // 7. Filtrar ofertas remotas usando Predicate
    public void ejemploPredicateRemoto() {
        System.out.println("PREDICATE - OFERTAS REMOTAS");

        Predicate<OfertaEmpleo> esRemoto = oferta -> oferta.getModalidad().equalsIgnoreCase("remote");

        ofertas.stream()
                .filter(esRemoto)
                .limit(10)
                .forEach(System.out::println);
    }

    // 8. Combinar dos predicados con and(): remota Y salario alto
    public void ejemploPredicateCombinado() {
        System.out.println("PREDICATE COMBINADO - REMOTAS CON SALARIO MAYOR A 150000");

        Predicate<OfertaEmpleo> esRemoto = oferta -> oferta.getModalidad().equalsIgnoreCase("remote");
        Predicate<OfertaEmpleo> salarioAlto = oferta -> oferta.getSalarioPromedio() > 150000;

        ofertas.stream()
                .filter(esRemoto.and(salarioAlto))
                .limit(10)
                .forEach(System.out::println);
    }

    // 9. Construir una descripcion personalizada de cada oferta usando Function
    public void ejemploFunction() {
        System.out.println("FUNCTION");

        Function<OfertaEmpleo, String> descripcion =
                oferta -> oferta.getTitulo() + " (" + oferta.getSeniority() + ") en "
                        + oferta.getEmpresa() + " - publicada " + oferta.getFechaPublicacion();

        ofertas.stream()
                .limit(10)
                .map(descripcion)
                .forEach(System.out::println);
    }

    // 10. Combinar cargo y salario en un texto tipo reporte usando BiFunction
    public void ejemploBiFunction() {
        System.out.println("BIFUNCTION");

        BiFunction<String, Double, String> reporte =
                (cargo, salario) -> "Puesto: " + cargo + " | Salario ofrecido: EUR "
                        + String.format(Locale.US, "%.2f", salario);

        ofertas.stream()
                .limit(10)
                .forEach(o -> System.out.println(reporte.apply(o.getTitulo(), o.getSalarioPromedio())));
    }

    // 11. Listar los cargos distintos
    public void cargosDistintos() {
        System.out.println("CARGOS DISTINTOS");

        ofertas.stream()
                .map(OfertaEmpleo::getTitulo)
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    // 12. Listar las industrias distintas
    public void industriasDistintas() {
        System.out.println("INDUSTRIAS DISTINTAS");

        ofertas.stream()
                .map(OfertaEmpleo::getIndustria)
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    // 13. Filtrar ofertas de la industria "Technology"
    public void filtrarPorIndustriaTecnologia() {
        System.out.println("OFERTAS DEL SECTOR TECHNOLOGY");

        ofertas.stream()
                .filter(o -> o.getIndustria().equalsIgnoreCase("Technology"))
                .limit(10)
                .forEach(System.out::println);
    }

    // 14. Transformar los cargos a mayusculas usando map
    public void mapMayusculas() {
        System.out.println("MAP - CARGOS EN MAYUSCULAS");

        ofertas.stream()
                .map(o -> o.getTitulo().toUpperCase())
                .distinct()
                .sorted()
                .forEach(System.out::println);
    }

    // 15. Calcular el salario promedio general
    public void salarioPromedio() {
        double promedio =
                ofertas.stream()
                        .mapToDouble(OfertaEmpleo::getSalarioPromedio)
                        .average()
                        .orElse(0);

        System.out.println("SALARIO PROMEDIO: EUR " + String.format(Locale.US, "%.2f", promedio));
    }

    // 16. Calcular estadisticas de salario (total, maximo, minimo) con summaryStatistics
    public void estadisticasSalario() {

        DoubleSummaryStatistics estadisticas =
                ofertas.stream()
                        .mapToDouble(OfertaEmpleo::getSalarioPromedio)
                        .summaryStatistics();

        System.out.println("ESTADISTICAS DE SALARIO");
        System.out.println("OFERTAS ANALIZADAS: " + estadisticas.getCount());
        System.out.println("TOTAL SALARIOS: EUR " + String.format(Locale.US, "%.2f", estadisticas.getSum()));
        System.out.println("SALARIO MAXIMO: EUR " + String.format(Locale.US, "%.2f", estadisticas.getMax()));
        System.out.println("SALARIO MINIMO: EUR " + String.format(Locale.US, "%.2f", estadisticas.getMin()));
    }

    // 17. Agrupar y contar ofertas por nivel de seniority
    public void ofertasPorSeniority() {
        System.out.println("OFERTAS POR NIVEL DE SENIORITY");

        Map<String, Long> resultado =
                ofertas.stream()
                        .collect(Collectors.groupingBy(
                                OfertaEmpleo::getSeniority,
                                Collectors.counting()));

        resultado.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }

    // 18. Salario promedio por industria (groupingBy + averagingDouble)
    public void salarioPromedioPorIndustria() {
        System.out.println("SALARIO PROMEDIO POR INDUSTRIA");

        Map<String, Double> resultado =
                ofertas.stream()
                        .collect(Collectors.groupingBy(
                                OfertaEmpleo::getIndustria,
                                Collectors.averagingDouble(OfertaEmpleo::getSalarioPromedio)));

        resultado.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(
                        e.getKey() + ": EUR " + String.format(Locale.US, "%.2f", e.getValue())));
    }

    // 19. Top 10 habilidades mas solicitadas (flatMap + groupingBy)
    public void topHabilidades() {
        System.out.println("TOP 10 HABILIDADES MAS SOLICITADAS");

        Map<String, Long> resultado =
                ofertas.stream()
                        .flatMap(o -> o.getHabilidades().stream())
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()));

        resultado.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(10)
                .forEach(System.out::println);
    }

    // 20. Generar un archivo de reporte con estadisticas generales
    public void generarReporte() throws IOException {

        DoubleSummaryStatistics estadisticas =
                ofertas.stream()
                        .mapToDouble(OfertaEmpleo::getSalarioPromedio)
                        .summaryStatistics();

        long remotas =
                ofertas.stream()
                        .filter(o -> o.getModalidad().equalsIgnoreCase("remote"))
                        .count();

        String industriaTop =
                ofertas.stream()
                        .collect(Collectors.groupingBy(OfertaEmpleo::getIndustria, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(NO_DISPONIBLE);

        String habilidadTop =
                ofertas.stream()
                        .flatMap(o -> o.getHabilidades().stream())
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.<String, Long>comparingByValue()
                                .thenComparing(Map.Entry.<String, Long>comparingByKey().reversed()))
                        .map(Map.Entry::getKey)
                        .orElse(NO_DISPONIBLE);

        List<String> reporte = List.of(
                "REPORTE DE OFERTAS DE CIENCIA DE DATOS 2025",
                "-------------------------------------------",
                "Total registros: " + estadisticas.getCount(),
                "Salario promedio: EUR " + String.format(Locale.US, "%.2f", estadisticas.getAverage()),
                "Salario maximo: EUR " + String.format(Locale.US, "%.2f", estadisticas.getMax()),
                "Salario minimo: EUR " + String.format(Locale.US, "%.2f", estadisticas.getMin()),
                "Ofertas remotas: " + remotas,
                "Industria con mas ofertas: " + industriaTop,
                "Habilidad mas solicitada: " + habilidadTop
        );

        Files.write(
                Paths.get("reporte_ofertas.txt"),
                reporte
        );

        System.out.println("\nReporte generado correctamente.");
    }
}
