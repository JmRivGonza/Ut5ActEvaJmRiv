package service;

import model.Nota;
import model.Usuario;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que gestiona el ciclo de vida de las notas (CRUD) almacenándolas
 * en un archivo de texto dentro del directorio personal de cada usuario.
 */
public class NotaService {

    private final Path userDirPath;
    private final Path notasFile;

    /**
     * Constructor que vincula el servicio al directorio de un usuario específico.
     * 
     * @param usuario El usuario que ha iniciado sesión.
     */
    public NotaService(Usuario usuario) {
        // Define la ruta: data/usuarios/nombre_usuario
        this.userDirPath = Path.of("data", "usuarios", usuario.getFormatoEmail());
        this.notasFile = userDirPath.resolve("notas.txt");

        // Crea el archivo de notas si es la primera vez que el usuario accede
        try {
            if (!Files.exists(notasFile)) {
                Files.createFile(notasFile);
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de notas" + e.getMessage());
        }
    }

    /**
     * Añade una nueva nota al final del archivo del usuario.
     */
    public void crearNota(Nota nota) {
        // Validación para evitar notas sin contenido
        if (nota == null || nota.getTitulo().isBlank() || nota.getContenido().isBlank()) {
            System.out.println("Error: La nota no puede estar vacia");
            return;
        }

        try {
            // 1) Leer notas actuales para mantener la persistencia
            List<String> notasExistentes = Files.readAllLines(notasFile);

            // 2) Modificar la lista en memoria añadiendo la nueva representación en String
            notasExistentes.add(nota.toString());

            // 3) Reescribir el fichero completo con la nueva lista incluida
            try (BufferedWriter writer = Files.newBufferedWriter(notasFile)) {
                for (String n : notasExistentes) {
                    writer.write(n);
                    writer.newLine();
                }
            }
            System.out.println("Nota creada exitosamente");
        } catch (IOException e) {
            System.out.println("Error al crear la nota: " + e.getMessage());
        }
    }

    /**
     * Lee todas las líneas del archivo y las formatea para mostrarlas por consola.
     */
    public void listarNotas() {
        try {
            List<String> lineas = Files.readAllLines(notasFile);
            if (lineas.isEmpty()) {
                System.out.println("No hay notas guardadas");
            } else {
                // Iteración con índice para mostrar el "ID" (número de línea + 1)
                for (int i = 0; i < lineas.size(); i++) {
                    String[] partes = lineas.get(i).split(";");
                    if (partes.length == 2) {
                        System.out.println("\n=== Nota numero " + (i + 1) + " ===");
                        System.out.println("Titulo: " + partes[0]);
                        System.out.println("Contenido: " + partes[1]);
                        System.out.println("===============================");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer las notas" + e.getMessage());
        }
    }

    /**
     * Muestra una única nota basándose en su posición en el archivo.
     * 
     * @param index Posición humana (empezando en 1).
     */
    public void verNota(int index) {
        try {
            List<String> lineas = Files.readAllLines(notasFile);
            // Validación de límites de la lista
            if (index > 0 && index <= lineas.size()) {
                String[] partes = lineas.get(index - 1).split(";");
                if (partes.length == 2) {
                    System.out.println("\n=== Nota numero " + index + " ===");
                    System.out.println("Titulo: " + partes[0]);
                    System.out.println("Contenido: " + partes[1]);
                    System.out.println("================================");
                }
            } else {
                System.out.println("Error: El numero de nota no existe");
            }
        } catch (Exception e) {
            System.out.println("Error al leer la nota" + e.getMessage());
        }
    }

    /**
     * Elimina una nota del archivo físico.
     * 
     * @param index Posición de la nota a borrar.
     */
    public void eliminarNota(int index) {
        try {
            // Se crea una ArrayList mutable para permitir la eliminación
            List<String> lineas = new ArrayList<>(Files.readAllLines(notasFile));
            if (index > 0 && index <= lineas.size()) {
                lineas.remove(index - 1); // Elimina de la lista en memoria
                Files.write(notasFile, lineas); // Sobrescribe el archivo con la lista actualizada
                System.out.println("Nota eliminada.");
            } else {
                System.out.println("Índice no válido.");
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar.");
        }
    }

    /**
     * Busca notas que contengan una palabra clave en el título o contenido.
     * 
     * @param palabraClave Término a buscar.
     */
    public void buscarNota(String palabraClave) {
        try {
            List<String> lineas = Files.readAllLines(notasFile);
            boolean encontrado = false;
            for (int i = 0; i < lineas.size(); i++) {
                String[] partes = lineas.get(i).split(";");
                // Comprobación de coincidencia parcial (case sensitive)
                if (partes.length == 2 && (partes[0].contains(palabraClave) || partes[1].contains(palabraClave))) {
                    System.out.println("\n=== [COINCIDENCIA] Nota numero " + (i + 1) + " ===");
                    System.out.println("Titulo: " + partes[0]);
                    System.out.println("Contenido: " + partes[1]);
                    System.out.println("===============================");
                    encontrado = true;
                }
            }
            if (!encontrado) {
                System.out.println("No se han encontrado notas con la palabra clave: " + palabraClave);
            }
        } catch (IOException e) {
            System.out.println("Error al buscar la nota" + e.getMessage());
        }
    }
}
