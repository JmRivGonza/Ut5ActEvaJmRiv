package app;

import model.Nota;
import model.Usuario;
import service.AuthService;
import service.NotaService;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interfaz de consola y el flujo de la
 * aplicación.
 * 
 * @author Jonay Rivero
 * @version 1.0
 */

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static AuthService authService = new AuthService();
    private static Usuario usuarioActual = null;
    private static NotaService notaService = null;

    /**
     * Punto de entrada del programa: controla el bucle principal de navegación.
     */
    public static void main(String[] args) {
        while (true) {
            if (usuarioActual == null) {
                if (menuPrincipal()) {
                    break;
                }
            } else {
                menuUsuario();
            }
        }
        System.out.println("Programa finalizado.");
    }

    /**
     * Gestiona las opciones para usuarios no identificados (Login, Registro,
     * Salir).
     * 
     * @return true si el usuario elige salir del programa.
     */
    private static boolean menuPrincipal() {
        System.out.println("\n====================");
        System.out.println("   Menú Principal   ");
        System.out.println("====================");
        System.out.println("1. Iniciar Sesión");
        System.out.println("2. Registrarse");
        System.out.println("3. Salir");
        System.out.println("====================");
        System.out.print("Selecciona una opción: ");
        String opcion = sc.nextLine();

        switch (opcion) {
            case "1":
                iniciarSesion();
                break;
            case "2":
                registrarse();
                break;
            case "3":
                return true;
            default:
                System.out.println("Opción no válida");
        }
        return false;
    }

    /**
     * Solicita credenciales y valida el acceso del usuario.
     */
    private static void iniciarSesion() {
        System.out.println("\n==================");
        System.out.println("  Iniciar Sesión  ");
        System.out.println("==================");
        System.out.print("Introduce el email: ");
        String email = sc.nextLine();
        System.out.print("Introduce la contraseña: ");
        String contraseña = sc.nextLine();

        // Validacion de que los campos no esten vacios
        if (email.isBlank() || contraseña.isBlank()) {
            System.out.println("Error: El email y la contraseña no pueden estar vacios");
            return;
        }

        try {
            Usuario usuario = authService.login(email, contraseña);
            if (usuario != null) {
                // Si el login es correcto, se establece la sesión y se inicializa el servicio
                // de notas 
                usuarioActual = usuario;
                notaService = new NotaService(usuario);
                System.out.println("Inicio de sesión exitoso, bienvenido " + usuario.getEmail());
            } else {
                System.out.println("ERROR: Email o contraseña incorrectos");
            }
        } catch (Exception e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }
    }

    /**
     * Gestiona el proceso de creación de una nueva cuenta de usuario.
     */
    private static void registrarse() {
        boolean completado = false;
        while (!completado) {
            System.out.println("\n==================");
            System.out.println("   Registrarse    ");
            System.out.println("==================");
            String email = "";
            String contraseña = "";
            System.out.println("\n--REGISTRO : (Escribe 'salir' para volver al menu principal)--");
            System.out.print("Introduce el email: ");
            email = sc.nextLine();

            // Permite al usuario cancelar el registro
            if (email.equalsIgnoreCase("salir")) {
                return;
            }
            System.out.print("Introduce la contraseña: ");
            contraseña = sc.nextLine();

            // Intenta registrar el usuario a través del servicio de autenticación
            if (authService.registrar(email, contraseña)) {
                completado = true;
            }
        }
    }

    /**
     * Muestra el menú de opciones para el usuario autenticado y gestiona la
     * navegación entre las diferentes funcionalidades.
     */
    private static void menuUsuario() {
        System.out.println("\n==================");
        System.out.println("   Menú Usuario   ");
        System.out.println("==================");
        System.out.println("1. Crear nota");
        System.out.println("2. Listar notas");
        System.out.println("3. Ver nota por número");
        System.out.println("4. Eliminar nota");
        System.out.println("5. Buscar nota por palabra clave");
        System.out.println("0. Cerrar sesión");
        System.out.println("==================");
        System.out.print("Selecciona una opción: ");
        String opcion = sc.nextLine();

        switch (opcion) {
            case "1":
                // Lógica para capturar datos y crear una nueva nota
                System.out.println("\n=== Has elegido 'crear nota' ===");
                System.out.println("\nIntroduce el titulo de la nota: ");
                String titulo = sc.nextLine();
                System.out.println("\nIntroduce el contenido de la nota: ");
                String contenido = sc.nextLine();
                notaService.crearNota(new Nota(titulo, contenido));
                break;
            case "2":
                // Muestra todas las notas del usuario actual
                System.out.println("\n=== Has elegido 'listar notas' ===");
                notaService.listarNotas();
                break;
            case "3":
                System.out.println("\n=== Has elegido 'ver nota por número' ===");
                try {
                    System.out.println("\nIntroduce el número de la nota: ");
                    int idVer = Integer.parseInt(sc.nextLine());
                    notaService.verNota(idVer);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debes introducir un numero valido");
                }

                break;
            case "4":
                // Elimina una nota específica validando que el ID sea numérico
                System.out.println("\n=== Has elegido 'eliminar nota' ===");
                try {
                    System.out.println("\nIntroduce el número de la nota: ");
                    int idEliminar = Integer.parseInt(sc.nextLine());
                    notaService.eliminarNota(idEliminar);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debes introducir un numero valido");
                }
                break;
            case "5":
                // Filtra las notas por un texto contenido en el título o cuerpo
                System.out.println("\n=== Has elegido 'buscar nota por palabra clave' ===");
                System.out.println("\nIntroduce la palabra clave: ");
                String palabraClave = sc.nextLine();
                notaService.buscarNota(palabraClave);
                break;
            case "0":
                // Limpia la sesión actual para volver al menú principal
                usuarioActual = null;
                notaService = null;
                System.out.println("Has cerrado sesion...");
                break;
            default:
                System.out.println("Error: Opción no válida");
        }
    }
}
