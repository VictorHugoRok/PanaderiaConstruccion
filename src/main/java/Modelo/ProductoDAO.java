package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa las operaciones CRUD (Crear, Leer, Actualizar y Eliminar) para la entidad {@link Producto}
 * en la base de datos. Esta clase utiliza JDBC para interactuar con la base de datos.
 * 
 * @implNote Esta clase implementa la interfaz {@link OperacionesDAO} con {@link Producto} como tipo de dato genérico.
 */
public class ProductoDAO implements OperacionesDAO<Producto> {
    
    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Constructor que inicializa la conexión a la base de datos y crea la tabla 'Producto' si no existe.
     * La tabla incluye los campos: id (clave primaria), nombre, descripcion, precio y tiempoElaboracion.
     */
    public ProductoDAO() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTableProducto = "CREATE TABLE IF NOT EXISTS Producto ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(100), "
                    + "descripcion TEXT, "
                    + "precio DOUBLE, "
                    + "tiempoElaboracion VARCHAR(50))";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableProducto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo producto a la base de datos.
     * 
     * @param producto El objeto {@link Producto} a agregar.
     * @return El ID del producto recién creado o -1 si ocurrió un error.
     */
    @Override
    public int agregar(Producto producto) {
        String sql = "INSERT INTO Producto (nombre, descripcion, precio, tiempoElaboracion) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setString(4, producto.getTiempoElaboracion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1)); // Asigna el ID generado
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id El ID del producto a obtener.
     * @return El objeto {@link Producto} con los datos del producto o {@code null} si no se encuentra el producto.
     */
    @Override
    public Producto obtenerPorId(int id) {
        String sql = "SELECT * FROM Producto WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

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
     * Obtiene todos los productos de la base de datos.
     * 
     * @return Una lista de objetos {@link Producto} que representan todos los productos en la base de datos.
     */
    @Override
    public List<Producto> obtenerTodos() {
        String sql = "SELECT * FROM Producto";
        List<Producto> productos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("tiempoElaboracion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    /**
     * Actualiza los datos de un producto en la base de datos.
     * 
     * @param id El ID del producto a actualizar.
     * @param productoActualizado El objeto {@link Producto} con los nuevos datos.
     */
    @Override
    public void actualizar(int id, Producto productoActualizado) {
        String sql = "UPDATE Producto SET nombre = ?, descripcion = ?, precio = ?, tiempoElaboracion = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, productoActualizado.getNombre());
            pstmt.setString(2, productoActualizado.getDescripcion());
            pstmt.setDouble(3, productoActualizado.getPrecio());
            pstmt.setString(4, productoActualizado.getTiempoElaboracion());
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     * 
     * @param id El ID del producto a eliminar.
     */
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Producto WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un producto por su nombre.
     * 
     * @param nombre El nombre del producto a buscar.
     * @return El objeto {@link Producto} correspondiente o {@code null} si no se encuentra el producto.
     */
    public Producto obtenerProductoPorNombre(String nombre) {
        String sql = "SELECT * FROM Producto WHERE nombre = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, nombre);  // Establecer el nombre del producto
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
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
        return null;  // Si no se encuentra el producto
    }

    /**
     * Elimina todos los productos de la base de datos.
     */
    public void eliminarTodos() {
        String sql = "DELETE FROM Producto";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una lista de productos a partir de una lista de IDs.
     * 
     * @param ids La lista de IDs de los productos a obtener.
     * @return Una lista de objetos {@link Producto} correspondientes a los IDs proporcionados.
     */
    public List<Producto> obtenerPorIds(List<Integer> ids) {
        List<Producto> productos = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return productos;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM Producto WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i < ids.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setInt(i + 1, ids.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getString("tiempoElaboracion")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
}