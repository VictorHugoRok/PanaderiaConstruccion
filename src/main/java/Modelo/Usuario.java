package Modelo;
/**
 * Representa un usuario en el sistema. Un usuario tiene un identificador, nombre, correo electrónico
 * y contraseña.
 */
public class Usuario {

    public int id;
    private String nombre;
    private String correo;
    private String contraseña;

    /**
     * Constructor para crear un usuario con un identificador, nombre y correo electrónico.
     * @param id El identificador único del usuario.
     * @param nombre El nombre del usuario.
     * @param correo El correo electrónico del usuario.
     */
    public Usuario(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    /**
     * Constructor para crear un usuario con nombre, correo electrónico y contraseña.
     * @param nombre El nombre del usuario.
     * @param correo El correo electrónico del usuario.
     * @param contraseña La contraseña del usuario.
     */
    public Usuario(String nombre, String correo, String contraseña) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    /**
     * Obtiene el identificador único del usuario.
     * @return El identificador del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * @param id El nuevo identificador del usuario.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return El correo electrónico del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param correo El nuevo correo electrónico del usuario.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña del usuario.
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contraseña La nueva contraseña del usuario.
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Devuelve una representación en cadena del objeto Usuario.
     * @return Una cadena que representa al usuario.
     */
    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", correo=" + correo + '}';
    }
}
