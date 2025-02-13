package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PedidoDAO implementa la interfaz OperacionesDAO para realizar
 * operaciones CRUD en la base de datos para la entidad Pedido.
 * <p>
 * Gestiona las tablas relacionadas con pedidos, incluyendo: - Pedido: Tabla
 * principal que almacena los detalles del pedido. - ProductoPedido: Tabla de
 * relación entre pedidos y productos.
 * <p>
 * Esta clase utiliza JDBC para interactuar con una base de datos H2.
 * Proporciona soporte para transacciones y asegura la integridad de los datos
 * entre las tablas relacionadas.
 */
public class PedidoDAO implements OperacionesDAO<Pedido> {

    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Constructor de PedidoDAO.
     * <p>
     * Inicializa la conexión con la base de datos y crea las tablas necesarias
     * si no existen: Pedido y ProductoPedido.
     */
    public PedidoDAO() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTablePedido = "CREATE TABLE IF NOT EXISTS Pedido ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "fechaEntrega VARCHAR(50), "
                    + "empleadoAsignadoId INT, "
                    + "prioridad INT, "
                    + "clienteId INT, "
                    + "FOREIGN KEY (clienteId) REFERENCES Cliente(id))";

            String createTableProductos = "CREATE TABLE IF NOT EXISTS ProductoPedido ("
                    + "pedidoId INT, "
                    + "productoId INT)";

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTablePedido);
                stmt.execute(createTableProductos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo pedido en la base de datos junto con sus productos
     * asociados.
     *
     * @param pedido El objeto Pedido que se desea insertar.
     * @return El ID generado del pedido insertado.
     * @throws SQLException si ocurre un error en la inserción o en la
     * transacción.
     */
    public int agregar(Pedido pedido) {
        String sqlPedido = "INSERT INTO Pedido (fechaEntrega, empleadoAsignadoId, prioridad, clienteId) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            try (PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                pstmtPedido.setString(1, pedido.getFechaEntrega());
                pstmtPedido.setInt(2, pedido.getEmpleadoAsignado().getId());
                pstmtPedido.setInt(3, pedido.getPrioridad());
                pstmtPedido.setInt(4, pedido.getCliente().getId());

                pstmtPedido.executeUpdate();
                try (ResultSet generatedKeys = pstmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int pedidoId = generatedKeys.getInt(1);
                        agregarProductos(pedidoId, pedido.getProductos());
                        connection.commit();
                        return pedidoId;
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Agrega productos a un pedido específico en la tabla intermedia
     * ProductoPedido.
     *
     * @param pedidoId El ID del pedido.
     * @param productos Lista de productos que se desean asociar al pedido.
     * @throws SQLException si ocurre un error durante la inserción.
     */
    private void agregarProductos(int pedidoId, List<Producto> productos) throws SQLException {
        String sqlProducto = "INSERT INTO ProductoPedido (pedidoId, productoId) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try (PreparedStatement pstmtProducto = connection.prepareStatement(sqlProducto)) {
                for (Producto producto : productos) {
                    pstmtProducto.setInt(1, pedidoId);
                    pstmtProducto.setInt(2, producto.getId());
                    pstmtProducto.addBatch();
                }
                pstmtProducto.executeBatch();
            }
        }
    }

    /**
     * Obtiene un pedido por su ID, incluyendo los datos relacionados como
     * empleado, cliente y productos.
     *
     * @param id El ID del pedido a buscar.
     * @return El objeto Pedido correspondiente, o null si no se encuentra.
     */
    @Override
    public Pedido obtenerPorId(int id) {
        String sqlPedido = "SELECT * FROM Pedido WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)) {

            pstmtPedido.setInt(1, id);
            try (ResultSet rsPedido = pstmtPedido.executeQuery()) {
                if (rsPedido.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rsPedido.getInt("id"));
                    pedido.setFechaEntrega(rsPedido.getString("fechaEntrega"));
                    pedido.setPrioridad(rsPedido.getInt("prioridad"));

                    int empleadoId = rsPedido.getInt("empleadoAsignadoId");
                    pedido.setEmpleadoAsignado(obtenerEmpleadoPorId(empleadoId));

                    int clienteId = rsPedido.getInt("clienteId");
                    pedido.setCliente(obtenerClientePorId(clienteId));

                    pedido.setProductos(obtenerProductos(id));
                    return pedido;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Empleado obtenerEmpleadoPorId(int id) {
        String sqlEmpleado = "SELECT * FROM Empleado WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmtEmpleado = connection.prepareStatement(sqlEmpleado)) {
            pstmtEmpleado.setInt(1, id);
            try (ResultSet rsEmpleado = pstmtEmpleado.executeQuery()) {
                if (rsEmpleado.next()) {
                    // Crear el objeto Empleado con los datos obtenidos
                    return new Empleado(
                            rsEmpleado.getInt("id"),
                            rsEmpleado.getString("nombre"),
                            rsEmpleado.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra el empleado, retornamos null
    }

    /**
     * Obtiene todos los pedidos asignados a un empleado específico.
     *
     * @param empleadoId El ID del empleado.
     * @return Una lista de pedidos asignados al empleado.
     */
    public List<Pedido> obtenerPedidosPorEmpleado(int empleadoId) {
        String sql = "SELECT * FROM Pedido WHERE empleadoAsignadoId = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, empleadoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setFechaEntrega(rs.getString("fechaEntrega"));
                    pedido.setPrioridad(rs.getInt("prioridad"));

                    pedido.setEmpleadoAsignado(obtenerEmpleadoPorId(empleadoId));

                    int clienteId = rs.getInt("clienteId");
                    pedido.setCliente(obtenerClientePorId(clienteId));

                    pedido.setProductos(obtenerProductos(rs.getInt("id")));
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    /**
     * Obtiene un cliente por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return El objeto Cliente correspondiente, o null si no se encuentra.
     */
    private Cliente obtenerClientePorId(int id) {
        String sqlCliente = "SELECT * FROM Cliente WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmtCliente = connection.prepareStatement(sqlCliente)) {
            pstmtCliente.setInt(1, id);
            try (ResultSet rsCliente = pstmtCliente.executeQuery()) {
                if (rsCliente.next()) {
                    return new Cliente(rsCliente.getInt("id"), rsCliente.getString("nombre"), rsCliente.getString("telefono"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los productos relacionados con un pedido específico.
     *
     * @param pedidoId El ID del pedido.
     * @return Una lista de productos asociados al pedido.
     * @throws SQLException si ocurre un error durante la consulta.
     */
    private List<Producto> obtenerProductos(int pedidoId) throws SQLException {
        String sqlProducto = "SELECT * FROM ProductoPedido WHERE pedidoId = ?";
        List<Producto> productos = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmtProducto = connection.prepareStatement(sqlProducto)) {
            pstmtProducto.setInt(1, pedidoId);
            try (ResultSet rsProducto = pstmtProducto.executeQuery()) {
                while (rsProducto.next()) {
                    int productoId = rsProducto.getInt("productoId");
                    Producto producto = obtenerProductoPorId(productoId);
                    if (producto != null) {
                        productos.add(producto);
                    }
                }
            }
        }
        return productos;
    }

    /**
 * Obtiene un pedido asignado a un empleado específico por el ID del empleado y el ID del pedido.
 * 
 * Este método realiza una consulta en la base de datos para recuperar un pedido que esté
 * asignado a un empleado con un ID determinado y que tenga el ID de pedido proporcionado.
 * Si se encuentra el pedido, se crea y devuelve un objeto {@link Pedido} con la información
 * del pedido, su cliente, los productos asociados y el empleado asignado.
 * 
 * @param empleadoId El ID del empleado al que está asignado el pedido.
 * @param pedidoId El ID del pedido que se desea obtener.
 * @return Un objeto {@link Pedido} con los detalles del pedido encontrado, o {@code null} si no se encuentra un pedido que coincida.
 */
public Pedido obtenerPedidoPorEmpleadoYId(int empleadoId, int pedidoId) {
    String sql = "SELECT * FROM Pedido WHERE empleadoAsignadoId = ? AND id = ?";
    Pedido pedido = null;

    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); 
         PreparedStatement pstmt = connection.prepareStatement(sql)) {

        pstmt.setInt(1, empleadoId);
        pstmt.setInt(2, pedidoId);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setFechaEntrega(rs.getString("fechaEntrega"));
                pedido.setPrioridad(rs.getInt("prioridad"));

                Empleado empleado = obtenerEmpleadoPorId(empleadoId);
                pedido.setEmpleadoAsignado(empleado);

                int clienteId = rs.getInt("clienteId");
                Cliente cliente = obtenerClientePorId(clienteId);
                pedido.setCliente(cliente);

                pedido.setProductos(obtenerProductos(rs.getInt("id")));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return pedido;
}


    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El objeto Producto correspondiente, o null si no se encuentra.
     */
    private Producto obtenerProductoPorId(int id) {
        String sql = "SELECT * FROM Producto WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getString("tiempoElaboracion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los pedidos registrados en la base de datos.
     *
     * @return Una lista de todos los pedidos.
     */
    @Override
    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM Pedido";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pedidos.add(new Pedido(
                        rs.getInt("id"),
                        obtenerClientePorId(rs.getInt("clienteId")),
                        rs.getInt("prioridad"),
                        obtenerProductos(rs.getInt("id")),
                        rs.getString("fechaEntrega"),
                        obtenerEmpleadoPorId(rs.getInt("empleadoAsignadoId"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    /**
     * Actualiza un pedido existente con nuevos datos.
     *
     * @param id El ID del pedido a actualizar.
     * @param pedidoActualizado El objeto Pedido con los datos actualizados.
     */
    @Override
    public void actualizar(int id, Pedido pedidoActualizado) {
        String sql = "UPDATE Pedido SET fechaEntrega = ?, empleadoAsignadoId = ?, prioridad = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, pedidoActualizado.getFechaEntrega());
            pstmt.setInt(2, pedidoActualizado.getEmpleadoAsignado().getId());
            pstmt.setInt(3, pedidoActualizado.getPrioridad());
            pstmt.setInt(4, id);
            pstmt.executeUpdate();

            eliminarProductos(connection, id);
            agregarProductos(id, pedidoActualizado.getProductos());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina todos los productos relacionados con un pedido.
     *
     * @param connection Conexión a la base de datos.
     * @param pedidoId El ID del pedido cuyos productos se eliminarán.
     * @throws SQLException si ocurre un error durante la eliminación.
     */
    private void eliminarProductos(Connection connection, int pedidoId) throws SQLException {
        String sql = "DELETE FROM ProductoPedido WHERE pedidoId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Elimina un pedido de la base de datos por su ID.
     *
     * @param id El ID del pedido a eliminar.
     */
    @Override
    public void eliminar(int id) {
        String sqlPedido = "DELETE FROM Pedido WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); PreparedStatement pstmtPedido = connection.prepareStatement(sqlPedido)) {

            eliminarProductos(connection, id);
            pstmtPedido.setInt(1, id);
            pstmtPedido.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina todos los registros de la tabla Pedido. También muestra
     * información sobre las tablas y columnas existentes en la base de datos.
     */
    public void eliminarTodos() {
        String sql = "DELETE FROM Pedido";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql2 = "SELECT TABLE_NAME, COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'PUBLIC' ORDER BY TABLE_NAME, ORDINAL_POSITION";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql2)) {

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                System.out.println("Tabla: " + tableName + ", Columna: " + columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
