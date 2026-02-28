package model;

/**
 * Representa una nota individual creada por un usuario.
 * 
 * @author Jonay Rivero
 * @version 1.0
 */

public class Nota {
    private String titulo;
    private String contenido;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    // Getters para recuperar los datos de la nota
    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    /**
     * Define el formato de persistencia en el archivo 'notas.txt'.
     * Separa el título del contenido mediante un punto y coma (;).
     * @return Título y contenido separados por punto y coma
     */
    @Override
    public String toString() {
        return titulo + ";" + contenido;
    }
}
