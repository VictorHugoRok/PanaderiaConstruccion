package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ClienteDAO.
 * Implementa las operaciones de acceso a datos para la entidad Cliente en la base de datos.
 * Esta clase permite agregar, obtener, actualizar, eliminar y consultar clientes en la base de datos.
 */
public class ClienteDAO implements OperacionesDAO<Cliente> {

    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Constructor de la clase ClienteDAO.
     * Inicializa la tabla Cliente en la base de datos si no existe.
     */
    public ClienteDAO() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTableCliente = "CREATE TABLE IF NOT EXISTS Cliente ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(100), "
                    + "telefono VARCHAR(15))";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableCliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo cliente a la base de datos.
     * 
     * @param cliente El cliente a agregar.
     * @return El ID del cliente recién agregado. Si ocurre un error, retorna -1.
     */
    @Override
    public int agregar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre, telefono) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getTelefono());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                    return generatedKeys.getInt(1); // Retorna el ID generado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Obtiene un cliente por su ID de la base de datos.
     * 
     * @param idCliente El ID del cliente a buscar.
     * @return El cliente con el ID especificado o null si no se encuentra.
     */
    @Override
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("telefono")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los clientes de la base de datos.
     * 
     * @return Una lista de todos los clientes.
     */
    @Override
    public List<Cliente> obtenerTodos() {
        String sql = "SELECT * FROM Cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    /**
     * Actualiza la información de un cliente en la base de datos.
     * 
     * @param idCliente        El ID del cliente a actualizar.
     * @param clienteActualizado El cliente con los nuevos datos.
     */
    @Override
    public void actualizar(int idCliente, Cliente clienteActualizado) {
        String sql = "UPDATE Cliente SET nombre = ?, telefono = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, clienteActualizado.getNombre());
            pstmt.setString(2, clienteActualizado.getTelefono());
            pstmt.setInt(3, idCliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     * 
     * @param idCliente El ID del cliente a eliminar.
     */
    @Override
    public void eliminar(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina todos los clientes de la base de datos.
     */
    public void eliminarTodos() {
        String sql = "DELETE FROM Cliente";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un cliente de la base de datos por su nombre.
     * 
     * @param nombre El nombre del cliente a buscar.
     * @return El cliente con el nombre especificado o null si no se encuentra.
     */
    public Cliente obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM Cliente WHERE nombre = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nombre); // Establece el valor del nombre a buscar
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("telefono")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
