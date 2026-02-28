package model;

public class Usuario {
    private String email;
    private String contraseña;

    public Usuario(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }

    public String getEmail() { return email; }
    public String getContraseña() { return contraseña; }

    public String getFormatoEmail() {
        return email.replaceAll("[^a-zA-Z0-9]", "_");
    }

    @Override
    public String toString() {
        return email + ";" + contraseña;
    }
}