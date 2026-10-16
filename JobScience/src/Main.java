import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ServicioOfertas servicio = null;

        try {
            servicio = new ServicioOfertas("data/data_science_job_posts_2025.csv");
        }
        catch (Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
            return;
        }

        int opcion;

        do {
            System.out.println("¡Hola, analista de ofertas de ciencia de datos!");
            System.out.println("Ingrese su número de opción: ");
            System.out.println("1. Total de registros");
            System.out.println("2. Mostrar primeros 10 registros");
            System.out.println("3. Ordenar por salario (descendente)");
            System.out.println("4. Ordenar por industria y salario");
            System.out.println("5. Mostrar cargo, empresa y salario de cada oferta");
            System.out.println("6. Filtrar ofertas con salario mayor a 150000");
            System.out.println("7. Filtrar ofertas remotas");
            System.out.println("8. Filtrar ofertas remotas con salario mayor a 150000");
            System.out.println("9. Mostrar una descripción de cada oferta");
            System.out.println("10. Mostrar reporte de puesto y salario ofrecido");
            System.out.println("11. Cargos distintos");
            System.out.println("12. Industrias distintas");
            System.out.println("13. Filtrar industria Technology");
            System.out.println("14. Mostrar cargos en mayúsculas");
            System.out.println("15. Salario promedio");
            System.out.println("16. Estadísticas de salario (total, máximo, mínimo)");
            System.out.println("17. Ofertas por nivel de seniority");
            System.out.println("18. Salario promedio por industria");
            System.out.println("19. Top 10 habilidades más solicitadas");
            System.out.println("20. Generar reporte (archivo .txt)");
            System.out.println("0. Para salir del programa");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            }
            catch (NumberFormatException e) {
                opcion = -1;
            }

            try {
                if (opcion == 1) {
                    servicio.totalRegistros();
                }
                else if (opcion == 2) {
                    servicio.mostrarPrimeros10();
                }
                else if (opcion == 3) {
                    servicio.ordenarPorSalario();
                }
                else if (opcion == 4) {
                    servicio.ordenarPorIndustriaYSalario();
                }
                else if (opcion == 5) {
                    servicio.ejemploConsumer();
                }
                else if (opcion == 6) {
                    servicio.ejemploPredicateSalario();
                }
                else if (opcion == 7) {
                    servicio.ejemploPredicateRemoto();
                }
                else if (opcion == 8) {
                    servicio.ejemploPredicateCombinado();
                }
                else if (opcion == 9) {
                    servicio.ejemploFunction();
                }
                else if (opcion == 10) {
                    servicio.ejemploBiFunction();
                }
                else if (opcion == 11) {
                    servicio.cargosDistintos();
                }
                else if (opcion == 12) {
                    servicio.industriasDistintas();
                }
                else if (opcion == 13) {
                    servicio.filtrarPorIndustriaTecnologia();
                }
                else if (opcion == 14) {
                    servicio.mapMayusculas();
                }
                else if (opcion == 15) {
                    servicio.salarioPromedio();
                }
                else if (opcion == 16) {
                    servicio.estadisticasSalario();
                }
                else if (opcion == 17) {
                    servicio.ofertasPorSeniority();
                }
                else if (opcion == 18) {
                    servicio.salarioPromedioPorIndustria();
                }
                else if (opcion == 19) {
                    servicio.topHabilidades();
                }
                else if (opcion == 20) {
                    servicio.generarReporte();
                }
                else if (opcion == 0) {
                    System.out.println("¡Gracias por usar el sistema de ofertas de ciencia de datos!");
                }
                else {
                    System.out.println("Opción no válida.");
                }
            }
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
        sc.close();
    }
}
