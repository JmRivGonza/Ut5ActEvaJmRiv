package service;

import model.Nota;
import model.Usuario;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NotaService {

    private final Path userDirPath;
    private final Path notasFile;

    public NotaService(Usuario usuario) {
        this.userDirPath = Path.of("data", "usuarios", usuario.getFormatoEmail());
        this.notasFile = userDirPath.resolve("notas.txt");

        try {
            if (!Files.exists(notasFile)) {
                Files.createFile(notasFile);
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo de notas" + e.getMessage());
        }
    }

    public void crearNota(Nota nota) {
    if (nota == null || nota.getTitulo().isBlank() || nota.getContenido().isBlank()) {
        System.out.println("Error: La nota no puede estar vacia");
        return;
    }

    try {
        // 1) Leer notas actuales
        List<String> notasExistentes = Files.readAllLines(notasFile);
        
        // 2) Modificar la lista en memoria añadiendo la nueva
        notasExistentes.add(nota.toString());
        
        // 3) Reescribir el fichero completo (sobrescribir)
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

    public void listarNotas() {
        try {
            List<String> lineas = Files.readAllLines(notasFile);
            if (lineas.isEmpty()) {
                System.out.println("No hay notas guardadas");
            } else {
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

    public void verNota(int index) {
        try {
            List<String> lineas = Files.readAllLines(notasFile);
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

    public void eliminarNota(int index) {
        try {
            List<String> lineas = new ArrayList<>(Files.readAllLines(notasFile));
            if (index > 0 && index <= lineas.size()) {
                lineas.remove(index - 1);
                Files.write(notasFile, lineas); // Sobrescribe el archivo con la lista actualizada
                System.out.println("Nota eliminada.");
            } else {
                System.out.println("Índice no válido.");
            }
        } catch (IOException e) {
            System.out.println("Error al eliminar.");
        }
    }

}
