package Modelo;

import java.util.List;

import java.util.List;

/**
 * Representa un pedido realizado por un cliente. Un pedido está compuesto por una lista de productos,
 * un cliente, un empleado asignado para la elaboración y otros detalles relacionados con la entrega y prioridad.
 */
public class Pedido {
    
    private int id;
    private List<Producto> productos;
    private String fechaEntrega;
    private Empleado empleadoAsignado;
    private String tiempoElaboracion;
    private int prioridad;
    private Cliente cliente;

    /**
     * Constructor de un pedido con todos los atributos necesarios.
     *
     * @param id El ID único del pedido.
     * @param cliente El cliente que realiza el pedido.
     * @param prioridad La prioridad del pedido.
     * @param productos La lista de productos que conforman el pedido.
     * @param fechaEntrega La fecha en que se debe entregar el pedido.
     * @param empleadoAsignado El empleado que está asignado para la elaboración del pedido.
     */
    public Pedido(int id, Cliente cliente, int prioridad, List<Producto> productos, String fechaEntrega, Empleado empleadoAsignado) {
        this.id = id;
        this.cliente = cliente;
        this.prioridad = prioridad;
        this.productos = productos;
        this.fechaEntrega = fechaEntrega;
        this.empleadoAsignado = empleadoAsignado;
    }

    /**
     * Constructor de un pedido sin el ID, usado al crear un nuevo pedido antes de ser persistido en la base de datos.
     *
     * @param cliente El cliente que realiza el pedido.
     * @param prioridad La prioridad del pedido.
     * @param productos La lista de productos que conforman el pedido.
     * @param fechaEntrega La fecha en que se debe entregar el pedido.
     * @param empleadoAsignado El empleado que está asignado para la elaboración del pedido.
     */
    public Pedido(Cliente cliente, int prioridad, List<Producto> productos, String fechaEntrega, Empleado empleadoAsignado) {
        this.cliente = cliente;
        this.prioridad = prioridad;
        this.productos = productos;
        this.fechaEntrega = fechaEntrega;
        this.empleadoAsignado = empleadoAsignado;
    }

    /**
     * Constructor vacío para la clase Pedido. Se utiliza para la creación de un objeto de pedido vacío.
     */
    Pedido() {
    }

    /**
     * Obtiene el cliente que realizó el pedido.
     *
     * @return El cliente que realizó el pedido.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente que realizó el pedido.
     *
     * @param cliente El cliente que realizó el pedido.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene la prioridad del pedido.
     *
     * @return La prioridad del pedido.
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad del pedido.
     *
     * @param prioridad La prioridad del pedido.
     */
    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Obtiene el ID del pedido.
     *
     * @return El ID del pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del pedido.
     *
     * @param id El ID del pedido.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene la lista de productos que forman el pedido.
     *
     * @return La lista de productos del pedido.
     */
    public List<Producto> getProductos() {
        return productos;
    }

    /**
     * Establece la lista de productos que forman el pedido.
     *
     * @param productos La lista de productos del pedido.
     */
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Obtiene la fecha de entrega del pedido.
     *
     * @return La fecha de entrega del pedido.
     */
    public String getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * Establece la fecha de entrega del pedido.
     *
     * @param fechaEntrega La fecha de entrega del pedido.
     */
    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * Obtiene el empleado asignado para la elaboración del pedido.
     *
     * @return El empleado asignado al pedido.
     */
    public Empleado getEmpleadoAsignado() {
        return empleadoAsignado;
    }

    /**
     * Establece el empleado asignado para la elaboración del pedido.
     *
     * @param empleadoAsignado El empleado que elaborará el pedido.
     */
    public void setEmpleadoAsignado(Empleado empleadoAsignado) {
        this.empleadoAsignado = empleadoAsignado;
    }

    /**
     * Obtiene el tiempo estimado para la elaboración del pedido.
     *
     * @return El tiempo de elaboración del pedido.
     */
    public String getTiempoElaboracion() {
        return tiempoElaboracion;
    }

    /**
     * Establece el tiempo estimado para la elaboración del pedido.
     *
     * @param tiempoElaboracion El tiempo de elaboración del pedido.
     */
    public void setTiempoElaboracion(String tiempoElaboracion) {
        this.tiempoElaboracion = tiempoElaboracion;
    }
}
