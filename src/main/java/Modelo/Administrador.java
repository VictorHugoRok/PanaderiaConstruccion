package Modelo;

/**
 * Clase Administrador.
 * Representa a un administrador que extiende de la clase Usuario.
 * Contiene los constructores necesarios para crear un administrador con diferentes conjuntos de datos.
 */
public class Administrador extends Usuario {

    /**
     * Constructor que inicializa un administrador con su ID, nombre y correo electrónico.
     *
     * @param id     El ID del administrador.
     * @param nombre El nombre del administrador.
     * @param correo El correo electrónico del administrador.
     */
    public Administrador(int id, String nombre, String correo) {
        super(id, nombre, correo);
    }

    /**
     * Constructor que inicializa un administrador con su nombre, correo electrónico y contraseña.
     *
     * @param nombre      El nombre del administrador.
     * @param correo      El correo electrónico del administrador.
     * @param contraseña  La contraseña del administrador.
     */
    public Administrador(String nombre, String correo, String contraseña) {
        super(nombre, correo, contraseña);
    }
}
