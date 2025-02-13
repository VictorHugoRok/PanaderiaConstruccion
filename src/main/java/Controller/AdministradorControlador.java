package Controller;

import Modelo.Administrador;
import Modelo.AdministradorDAO;
import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Empleado;
import Modelo.EmpleadoDAO;
import Modelo.Pedido;
import Modelo.PedidoDAO;
import Modelo.Producto;
import Modelo.ProductoDAO;
import java.util.List;

/**
 * Clase AdministradorControlador.
 * Controlador que facilita la interacción con los DAOs relacionados con empleados, productos, pedidos, clientes y administradores.
 */
public class AdministradorControlador {
    private ProductoDAO productoDAO;
    private PedidoDAO pedidoDAO;
    private ClienteDAO clienteDAO;
    private EmpleadoDAO empleadoDAO;
    private AdministradorDAO administradorDAO;
    private Administrador admin;

    /**
     * Constructor que inicializa los DAOs y asigna un administrador.
     *
     * @param admin El administrador que utilizará el controlador.
     */
    public AdministradorControlador(Administrador admin) {
        this.admin = admin;
        clienteDAO = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
        productoDAO = new ProductoDAO();
        pedidoDAO = new PedidoDAO();
        administradorDAO = new AdministradorDAO();
    }

    // Métodos para manejar empleados

    /**
     * Obtiene la lista de todos los empleados.
     *
     * @return Lista de empleados.
     */
    public List<Empleado> verEmpleados() {
        return empleadoDAO.obtenerTodos();
    }

    /**
     * Añade un nuevo empleado.
     *
     * @param empleado El empleado a añadir.
     */
    public void añadirEmpleado(Empleado empleado) {
        empleadoDAO.agregar(empleado);
    }

    /**
     * Edita un empleado existente.
     *
     * @param idEmpleado           El ID del empleado a editar.
     * @param empleadoActualizado El empleado con los datos actualizados.
     */
    public void editarEmpleado(int idEmpleado, Empleado empleadoActualizado) {
        empleadoDAO.actualizar(idEmpleado, empleadoActualizado);
    }

    /**
     * Elimina un empleado por su ID.
     *
     * @param idEmpleado El ID del empleado a eliminar.
     */
    public void eliminarEmpleado(int idEmpleado) {
        empleadoDAO.eliminar(idEmpleado);
    }

    /**
     * Elimina todos los empleados.
     */
    public void eliminarTodosEmpleados() {
        empleadoDAO.eliminarTodos();
    }

    /**
     * Obtiene un empleado por su ID.
     *
     * @param idEmpleado El ID del empleado a buscar.
     * @return El empleado encontrado, o null si no existe.
     */
    public Empleado obtenerEmpleadoPorId(int idEmpleado) {
        return empleadoDAO.obtenerPorId(idEmpleado);
    }

    /**
     * Obtiene un empleado por su correo.
     *
     * @param correoEmpleado El correo del empleado a buscar.
     * @return El empleado encontrado, o null si no existe.
     */
    public Empleado obtenerEmpleadoPorCorreo(String correoEmpleado) {
        return empleadoDAO.obtenerPorCorreo(correoEmpleado);
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
     * @param idProducto           El ID del producto a editar.
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

    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado, o null si no existe.
     */
    public Producto obtenerProductoPorId(int id) {
        return productoDAO.obtenerPorId(id);
    }

    /**
     * Obtiene un producto por su nombre.
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return El producto encontrado, o null si no existe.
     */
    public Producto obtenerProductoPorNombre(String nombreProducto) {
        return productoDAO.obtenerProductoPorNombre(nombreProducto);
    }

    /**
     * Obtiene una lista de productos por sus IDs.
     *
     * @param ids Lista de IDs de los productos a buscar.
     * @return Lista de productos encontrados.
     */
    public List<Producto> obtenerProductosPorIds(List<Integer> ids) {
        return productoDAO.obtenerPorIds(ids);
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
     * @param idPedido           El ID del pedido a editar.
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

    // Métodos para manejar clientes

    /**
     * Obtiene la lista de todos los clientes.
     *
     * @return Lista de clientes.
     */
    public List<Cliente> verClientes() {
        return clienteDAO.obtenerTodos();
    }

    /**
     * Añade un nuevo cliente.
     *
     * @param cliente El cliente a añadir.
     * @return El ID del cliente agregado.
     */
    public int añadirCliente(Cliente cliente) {
        return clienteDAO.agregar(cliente);
    }

    /**
     * Edita un cliente existente.
     *
     * @param idCliente           El ID del cliente a editar.
     * @param clienteActualizado El cliente con los datos actualizados.
     */
    public void editarCliente(int idCliente, Cliente clienteActualizado) {
        clienteDAO.actualizar(idCliente, clienteActualizado);
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param idCliente El ID del cliente a eliminar.
     */
    public void eliminarCliente(int idCliente) {
        clienteDAO.eliminar(idCliente);
    }

    /**
     * Elimina todos los clientes.
     */
    public void eliminarTodosClientes() {
        clienteDAO.eliminarTodos();
    }

    /**
     * Obtiene un cliente por su nombre.
     *
     * @param nombre El nombre del cliente a buscar.
     * @return El cliente encontrado, o null si no existe.
     */
    public Cliente obtenerClientePorNombre(String nombre) {
        return clienteDAO.obtenerPorNombre(nombre);
    }

    // Métodos para manejar administradores

    /**
     * Añade un nuevo administrador.
     *
     * @param administrador El administrador a añadir.
     * @return El ID del administrador agregado.
     */
    public int añadirAdministrador(Administrador administrador) {
        return administradorDAO.agregar(administrador);
    }

    /**
     * Obtiene la lista de todos los administradores.
     *
     * @return Lista de administradores.
     */
    public List<Administrador> getAdministradores() {
        return administradorDAO.obtenerTodos();
    }
}

