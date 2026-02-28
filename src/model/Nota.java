package model;

public class Nota {
    private String titulo;
    private String contenido;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }

    @Override
    public String toString() {
        return titulo + ";" + contenido;
    }
}
