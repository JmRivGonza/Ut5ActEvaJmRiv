## Sistema de Notas por Usuarios

Autor: Jonay Rivero
Act.Ev UT5.1

* **Ejecución:** Para poder acceder a la aplicación, se debe ejecutar el archivo 'Main.java' ubicado en el paquete 'app'.

* **Gestión de Usuarios:** Creación de perfiles de usuario detallados con email y contraseña, incluyendo la creación automática de directorios personalizados para cada uno.
* **Control de Sesiones y Notas:** 
    * Registro e inicio de sesión con verificación de credenciales contra el archivo usuarios.txt.
    * Lógica de panel dinámica: Crear nota, Listar notas, Ver nota por número y Eliminar nota.
    * Restricción de flujo: Aislamiento total de datos; cada usuario solo puede gestionar su propio panel de notas almacenado en su carpeta personal.
    * Funcionalidad Extra: Buscador avanzado de notas por palabra clave en título o contenido.
* **Sistema de Persistencia Automático:** 
    * Almacenamiento de credenciales en data/usuarios.txt y de notas en data/usuarios/<email_sanitizado>/notas.txt.
    * Reescritura dinámica de ficheros: Al eliminar una nota, el sistema lee el archivo, modifica la lista en memoria y sobrescribe el fichero para mantener la integridad.
* **Manejo de Excepciones y Resúmenes:**
    * Menús a prueba de errores: Uso de bloques try-catch para capturar errores de formato (como introducir letras cuando se esperan números).
    * Implementación de try-with-resources y la API java.nio.file para asegurar el cierre automático de flujos de datos y una gestión moderna de rutas.
    * Batería de comprobaciones para índices incorrectos, usuarios inexistentes o errores de lectura/escritura.

* **Organización:**
|-- src
|   |-- app
|   |   |-- Main.java
|   |-- model
|   |   |-- Nota.java
|   |   |-- Usuario.java
|   |-- service
|   |   |-- AuthService.java
|   |   |-- NotaService.java
|
|-- data
|   |-- usuarios.txt
|   |-- usuarios/
|
|-- .gitignore
|-- README.md