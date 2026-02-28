package service;

import model.Usuario;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AuthService {
    private final Path DATA_DIR = Path.of("data");
    private final Path USUARIOS_FILE = DATA_DIR.resolve("usuarios.txt");
    private final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    public AuthService() {
        inicializarFicheros();
    }

    private void inicializarFicheros(){
        try {
            Files.createDirectories(DATA_DIR);
            Files.createDirectories(USUARIOS_DIR);
            if (!Files.exists(USUARIOS_FILE)){
                Files.createFile(USUARIOS_FILE);
            }
        } catch (IOException e) {
            System.out.println("Error critico inciializando ficheros: " + e.getMessage());
        }
    }

    public boolean registrar(String email, String contraseña) {
    if (email == null || email.isBlank() || contraseña == null || contraseña.isBlank()) {
        System.out.println("Error: El email y la contraseña no pueden estar vacios");
        return false;
    }

    if (existeUsuario(email)) {
        System.out.println("Error: El email ya esta registrado");
        return false;
    }

    try {
        // 1) Leemos todas las líneas existentes (como en el ejemplo de modificar)
        List<String> usuarios = Files.readAllLines(USUARIOS_FILE);
        
        // 2) Añadimos el nuevo usuario a la lista en memoria
        usuarios.add(email + ";" + contraseña);
        
        // 3) Sobrescribimos el archivo con la lista completa usando BufferedWriter
        try (BufferedWriter writer = Files.newBufferedWriter(USUARIOS_FILE)) {
            for (String u : usuarios) {
                writer.write(u);
                writer.newLine();
            }
        }

        // Crear la carpeta del usuario
        Usuario nuevoUsuario = new Usuario(email, contraseña);
        Files.createDirectories(USUARIOS_DIR.resolve(nuevoUsuario.getFormatoEmail()));
        
        System.out.println("Usuario registrado exitosamente");
        return true;
    } catch (IOException e) {
        System.out.println("Error al registrar: " + e.getMessage());
        return false;
    }
}

    public Usuario login (String email, String contraseña){
     try (BufferedReader br = Files.newBufferedReader(USUARIOS_FILE)){
        String linea;

        if (email == null || email.isBlank() || contraseña == null || contraseña.isBlank()){
            System.out.println("Error: El email y la contraseña no pueden estar vacios");
            return null;
        }
        
        while ((linea = br.readLine()) != null){
            String[] partes = linea.split(";");
            if (partes.length == 2 && partes[0].equals(email) && partes[1].equals(contraseña)){
                return new Usuario(email, contraseña);
            }
        }
     } catch (IOException e) {
        System.out.println("Error al iniciar sesion: " + e.getMessage());
     }

     System.out.println("Credenciales incorrectas para el usuario: " + email);
     return null;
    }


    public boolean existeUsuario(String email){
        try (BufferedReader br = Files.newBufferedReader(USUARIOS_FILE)){
            String linea;
            while ((linea = br.readLine()) != null){
                String[] partes = linea.split(";");
                if (partes.length >= 1 && partes[0].equalsIgnoreCase(email)){
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al verificar el usuario: " + e.getMessage());
        }
        return false;
    }
}
