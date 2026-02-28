package model;

/**
 * Clase que representa un usuario con email y contraseña.
 * @author Jonay Rivero
 * @version 1.0
 */
public class Usuario {
    private String email;
    private String contraseña;

    public Usuario(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }

    // Getters básicos para acceder a la información del perfil
    public String getEmail() { return email; }
    public String getContraseña() { return contraseña; }

    /**
     * Sanitiza el email para usarlo como nombre de carpeta.
     * Reemplaza cualquier carácter no alfanumérico por un guion bajo.
     * Ejemplo: "test@mail.com" -> "test_mail_com"
     * @return Email sanitizado para usarlo como nombre de carpeta
     */
    public String getFormatoEmail() {
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }

    /**
     * Define cómo se guarda el usuario en el archivo 'usuarios.txt'.
     * Utiliza el punto y coma (;) como delimitador de campos.
     * @return Email y contraseña separados por punto y coma
     */
    @Override
    public String toString() {
        return email + ";" + contraseña;
    }
}