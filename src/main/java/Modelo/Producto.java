package Modelo;

/**
 * Representa un producto en el sistema. Un producto tiene un identificador, nombre, descripción, precio
 * y tiempo de elaboración.
 */
public class Producto {
    
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String tiempoElaboracion;
    
    /**
     * Constructor completo para crear un producto con todos sus atributos.
     * @param id El identificador único del producto.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción detallada del producto.
     * @param precio El precio del producto.
     * @param tiempoElaboracion El tiempo estimado para elaborar el producto.
     */
    public Producto(int id, String nombre, String descripcion, double precio, String tiempoElaboracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tiempoElaboracion = tiempoElaboracion;
    }

    /**
     * Constructor para crear un producto sin el identificador. Usado generalmente al agregar productos nuevos.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción del producto.
     * @param precio El precio del producto.
     * @param tiempoElaboracion El tiempo estimado para elaborar el producto.
     */
    public Producto(String nombre, String descripcion, double precio, String tiempoElaboracion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tiempoElaboracion = tiempoElaboracion;
    }
    
    /**
     * Constructor para crear un producto con nombre y descripción. Este constructor podría ser útil si el precio
     * y tiempo de elaboración no son necesarios de inmediato.
     * @param nombre El nombre del producto.
     * @param descripcion La descripción del producto.
     */
    public Producto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador único del producto.
     * @return El identificador del producto.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del producto.
     * @param id El nuevo identificador del producto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El nuevo nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del producto.
     * @return La descripción del producto.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del producto.
     * @param descripcion La nueva descripción del producto.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio del producto.
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * @param precio El nuevo precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el tiempo de elaboración del producto.
     * @return El tiempo estimado para elaborar el producto.
     */
    public String getTiempoElaboracion() {
        return tiempoElaboracion;
    }

    /**
     * Establece el tiempo de elaboración del producto.
     * @param tiempoElaboracion El nuevo tiempo de elaboración del producto.
     */
    public void setTiempoElaboracion(String tiempoElaboracion) {
        this.tiempoElaboracion = tiempoElaboracion;
    }
}
