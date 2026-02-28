package service;

import model.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Servicio encargado de la autenticación, registro y persistencia de usuarios
 * en el sistema de archivos.
 * 
 * @author Jonay Rivero
 * @version 1.0
 */
public class AuthService {
    // Definición de rutas para el almacenamiento de datos
    private final Path DATA_DIR = Path.of("data");
    private final Path USUARIOS_FILE = DATA_DIR.resolve("usuarios.txt");
    private final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    public AuthService() {
        inicializarFicheros();
    }

    /**
     * Crea la estructura de directorios y archivos necesaria si no existen al
     * arrancar el servicio.
     */
    private void inicializarFicheros() {
        try {
            Files.createDirectories(DATA_DIR);
            Files.createDirectories(USUARIOS_DIR);
            if (!Files.exists(USUARIOS_FILE)) {
                Files.createFile(USUARIOS_FILE);
            }
        } catch (IOException e) {
            System.out.println("Error critico inciializando ficheros: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario si el email no existe y crea su directorio
     * personal.
     * 
     * @param email      Email del usuario a registrar.
     * @param contraseña Contraseña del usuario.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrar(String email, String contraseña) {
        // Validacion de que los campos no esten vacios
        if (email == null || email.isBlank() || contraseña == null || contraseña.isBlank()) {
            System.out.println("Error: El email y la contraseña no pueden estar vacios");
            return false;
        }

        // Validacion de que el email no este registrado
        if (existeUsuario(email)) {
            System.out.println("Error: El email ya esta registrado");
            return false;
        }

        try {
            // 1) Lectura: Obtenemos todos los usuarios actuales para no perder datos al
            // sobrescribir
            List<String> usuarios = Files.readAllLines(USUARIOS_FILE);

            // 2) Actualización en memoria: Añadimos el nuevo par de credenciales
            // (email;password)
            usuarios.add(email + ";" + contraseña);

            // 3) Escritura: Sobrescribimos el archivo con la lista actualizada
            try (BufferedWriter writer = Files.newBufferedWriter(USUARIOS_FILE)) {
                for (String u : usuarios) {
                    writer.write(u);
                    writer.newLine();
                }
            }

            // 4) Infraestructura: Creamos una carpeta física específica para este usuario
            // (para sus notas)
            Usuario nuevoUsuario = new Usuario(email, contraseña);
            Files.createDirectories(USUARIOS_DIR.resolve(nuevoUsuario.getFormatoEmail()));

            System.out.println("Usuario registrado exitosamente");
            return true;
        } catch (

        IOException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida las credenciales de un usuario y devuelve el objeto Usuario si son
     * correctas.
     * 
     * @param email      Email del usuario.
     * @param contraseña Contraseña del usuario.
     * @return Objeto Usuario si las credenciales son válidas, null en caso
     *         contrario.
     */
    public Usuario login(String email, String contraseña) {
        try (BufferedReader br = Files.newBufferedReader(USUARIOS_FILE)) {
            String linea;

            // Validacion de que los campos no esten vacios antes de procesar el archivo
            if (email == null || email.isBlank() || contraseña == null || contraseña.isBlank()) {
                System.out.println("Error: El email y la contraseña no pueden estar vacios");
                return null;
            }

            // Lectura secuencial línea por línea buscando coincidencia exacta
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                // Verificamos formato y comparamos email y contraseña
                if (partes.length == 2 && partes[0].equals(email) && partes[1].equals(contraseña)) {
                    return new Usuario(email, contraseña);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al iniciar sesion: " + e.getMessage());
        }

        System.out.println("Credenciales incorrectas para el usuario: " + email);
        return null;
    }

    /**
     * Verifica si un email ya está presente en el archivo de usuarios.
     * @return true si el email ya existe (ignora mayúsculas/minúsculas).
     */
    public boolean existeUsuario(String email) {
        try (BufferedReader br = Files.newBufferedReader(USUARIOS_FILE)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                // Comparamos solo la primera parte de la línea (el email)
                if (partes.length >= 1 && partes[0].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar el usuario: " + e.getMessage());
        }
        return false;
    }
}
