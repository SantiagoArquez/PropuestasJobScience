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

    }

