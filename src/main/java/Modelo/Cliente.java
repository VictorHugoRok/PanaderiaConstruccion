package Modelo;

/**
 * Clase Cliente.
 * Representa a un cliente con un ID, nombre y número de teléfono.
 * Proporciona métodos para acceder y modificar los atributos del cliente.
 */
public class Cliente {

    private int idCliente;
    private String nombre;
    private String telefono;

    /**
     * Constructor para crear un cliente con un ID, nombre y teléfono.
     *
     * @param idCliente El ID del cliente.
     * @param nombre    El nombre del cliente.
     * @param telefono  El número de teléfono del cliente.
     */
    public Cliente(int idCliente, String nombre, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    /**
     * Constructor para crear un cliente con un nombre y teléfono.
     *
     * @param nombreCliente El nombre del cliente.
     * @param telefonoCliente El número de teléfono del cliente.
     */
    public Cliente(String nombreCliente, String telefonoCliente) {
        this.nombre = nombreCliente;
        this.telefono = telefonoCliente;
    }

    /**
     * Obtiene el ID del cliente.
     *
     * @return El ID del cliente.
     */
    public int getId() {
        return idCliente;
    }

    /**
     * Establece el ID del cliente.
     *
     * @param idCliente El ID a asignar al cliente.
     */
    public void setId(int idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return El nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre El nombre a asignar al cliente.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     *
     * @return El número de teléfono del cliente.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del cliente.
     *
     * @param telefono El número de teléfono a asignar al cliente.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
