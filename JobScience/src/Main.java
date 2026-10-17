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
