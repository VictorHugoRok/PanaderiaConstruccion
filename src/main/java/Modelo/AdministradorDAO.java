package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase AdministradorDAO.
 * Implementa las operaciones de acceso a datos para la entidad Administrador.
 * Permite realizar las operaciones CRUD sobre la base de datos, así como la validación
 * de credenciales de administradores y la gestión de su información.
 */
public class AdministradorDAO implements OperacionesDAO<Administrador> {

    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Constructor que inicializa la tabla Administrador en la base de datos si no existe.
     */
    public AdministradorDAO() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTableAdministrador = "CREATE TABLE IF NOT EXISTS Administrador ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(100), "
                    + "email VARCHAR(100), "
                    + "contraseña VARCHAR(100))";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableAdministrador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Valida si un administrador existe con el correo y la contraseña proporcionados.
     *
     * @param correo      El correo electrónico del administrador.
     * @param contraseña  La contraseña del administrador.
     * @return true si el administrador es válido, false en caso contrario.
     */
    public boolean validarAdministrador(String correo, String contraseña) {
        boolean esValido = false;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM ADMINISTRADOR WHERE email = ? AND contraseña = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, correo);
                stmt.setString(2, contraseña);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    esValido = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return esValido;
    }

    /**
     * Agrega un nuevo administrador a la base de datos.
     *
     * @param administrador El administrador a agregar.
     * @return El ID del administrador generado en la base de datos.
     */
    @Override
    public int agregar(Administrador administrador) {
        String sql = "INSERT INTO Administrador (nombre, email, contraseña) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, administrador.getNombre());
            pstmt.setString(2, administrador.getCorreo());
            pstmt.setString(3, administrador.getContraseña());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retorna el ID generado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Obtiene un administrador por su ID.
     *
     * @param id El ID del administrador a obtener.
     * @return El administrador correspondiente al ID, o null si no se encuentra.
     */
    @Override
    public Administrador obtenerPorId(int id) {
        String sql = "SELECT * FROM Administrador WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obtiene todos los administradores de la base de datos.
     *
     * @return Una lista de todos los administradores.
     */
    @Override
    public List<Administrador> obtenerTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM Administrador";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                administradores.add(new Administrador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return administradores;
    }

    /**
     * Actualiza la información de un administrador en la base de datos.
     *
     * @param id                    El ID del administrador a actualizar.
     * @param administradorActualizado El objeto Administrador con la información actualizada.
     */
    @Override
    public void actualizar(int id, Administrador administradorActualizado) {
        String sql = "UPDATE Administrador SET nombre = ?, email = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, administradorActualizado.getNombre());
            pstmt.setString(2, administradorActualizado.getCorreo());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un administrador de la base de datos por su ID.
     *
     * @param id El ID del administrador a eliminar.
     */
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Administrador WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina todos los administradores de la base de datos.
     */
    public void eliminarTodos() {
        String sql = "DELETE FROM Administrador";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un administrador por su correo electrónico.
     *
     * @param correo El correo electrónico del administrador a buscar.
     * @return El administrador correspondiente al correo, o null si no se encuentra.
     */
    public Administrador obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM Administrador WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
