package Controller;

import Modelo.Administrador;
import Modelo.Cliente;
import Modelo.Empleado;
import Modelo.Pedido;
import Modelo.PedidoDAO;
import Modelo.Producto;
import Modelo.ProductoDAO;
import java.util.List;

/**
 * Clase EmpleadoControlador.
 * Controlador que facilita la interacción con los DAOs relacionados con productos y pedidos 
 * para las operaciones realizadas por un empleado.
 */
public class EmpleadoControlador {

    private Empleado empleado;
    private ProductoDAO productoDAO;
    private PedidoDAO pedidoDAO;

    /**
     * Constructor que inicializa el controlador con un empleado.
     *
     * @param empleado El empleado asociado al controlador.
     */
    public EmpleadoControlador(Empleado empleado) {
        this.empleado = empleado;
        this.productoDAO = new ProductoDAO();
        this.pedidoDAO = new PedidoDAO();
    }

    // Métodos para manejar productos

    /**
     * Obtiene la lista de todos los productos.
     *
     * @return Lista de productos.
     */
    public List<Producto> verProductos() {
        return productoDAO.obtenerTodos();
    }

    /**
     * Añade un nuevo producto.
     *
     * @param producto El producto a añadir.
     */
    public void añadirProducto(Producto producto) {
        productoDAO.agregar(producto);
    }

    /**
     * Edita un producto existente.
     *
     * @param idProducto          El ID del producto a editar.
     * @param productoActualizado El producto con los datos actualizados.
     */
    public void editarProducto(int idProducto, Producto productoActualizado) {
        productoDAO.actualizar(idProducto, productoActualizado);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param idProducto El ID del producto a eliminar.
     */
    public void eliminarProducto(int idProducto) {
        productoDAO.eliminar(idProducto);
    }

    /**
     * Elimina todos los productos.
     */
    public void eliminarTodosProductos() {
        productoDAO.eliminarTodos();
    }

    // Métodos para manejar pedidos

    /**
     * Obtiene la lista de todos los pedidos.
     *
     * @return Lista de pedidos.
     */
    public List<Pedido> verPedidos() {
        return pedidoDAO.obtenerTodos();
    }

    /**
     * Añade un nuevo pedido.
     *
     * @param pedido El pedido a añadir.
     */
    public void añadirPedido(Pedido pedido) {
        pedidoDAO.agregar(pedido);
    }

    /**
     * Edita un pedido existente.
     *
     * @param idPedido          El ID del pedido a editar.
     * @param pedidoActualizado El pedido con los datos actualizados.
     */
    public void editarPedido(int idPedido, Pedido pedidoActualizado) {
        pedidoDAO.actualizar(idPedido, pedidoActualizado);
    }

    /**
     * Elimina un pedido por su ID.
     *
     * @param idPedido El ID del pedido a eliminar.
     */
    public void eliminarPedido(int idPedido) {
        pedidoDAO.eliminar(idPedido);
    }

    /**
     * Elimina todos los pedidos.
     */
    public void eliminarTodosPedidos() {
        pedidoDAO.eliminarTodos();
    }

    /**
     * Obtiene una lista de pedidos asociados a un empleado por su ID.
     *
     * @param empleadoId El ID del empleado.
     * @return Lista de pedidos asociados al empleado.
     */
    public List<Pedido> obtenerPedidosPorEmpleado(int empleadoId) {
        return pedidoDAO.obtenerPedidosPorEmpleado(empleadoId);
    }

    /**
     * Obtiene un pedido específico asociado a un empleado por su ID y el ID del pedido.
     *
     * @param empleadoId El ID del empleado.
     * @param pedidoId   El ID del pedido.
     * @return El pedido encontrado, o null si no existe.
     */
    public Pedido obtenerPedidoPorEmpleadoYId(int empleadoId, int pedidoId) {
        return pedidoDAO.obtenerPedidoPorEmpleadoYId(empleadoId, pedidoId);
    }

    // Métodos adicionales para manejar productos

    /**
     * Obtiene una lista de productos por sus IDs.
     *
     * @param ids Lista de IDs de los productos a buscar.
     * @return Lista de productos encontrados.
     */
    public List<Producto> obtenerProductosPorIds(List<Integer> ids) {
        return productoDAO.obtenerPorIds(ids);
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado, o null si no existe.
     */
    public Producto obtenerProductoPorId(int id) {
        return productoDAO.obtenerPorId(id);
    }
}
