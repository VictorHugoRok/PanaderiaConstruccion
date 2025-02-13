package Modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase EmpleadoDAO que implementa las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@link Empleado} en la base de datos.
 * Implementa la interfaz {@link OperacionesDAO}.
 */
public class EmpleadoDAO implements OperacionesDAO<Empleado> {
    
    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Constructor de la clase EmpleadoDAO. Este constructor crea la tabla de empleados
     * si no existe en la base de datos.
     */
    public EmpleadoDAO() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String createTableEmpleado = "CREATE TABLE IF NOT EXISTS Empleado ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "nombre VARCHAR(100), "
                    + "email VARCHAR(100), "
                    + "contraseña VARCHAR(100))";  // Removed 'puesto' field as per your request
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableEmpleado);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo empleado a la base de datos.
     * 
     * @param empleado El empleado a agregar.
     * @return El ID del empleado recién agregado.
     */
    @Override
    public int agregar(Empleado empleado) {
        String sql = "INSERT INTO Empleado (nombre, email, contraseña) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getCorreo());
            pstmt.setString(3, empleado.getContraseña());
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
     * Valida las credenciales de un empleado para iniciar sesión.
     * 
     * @param correo     El correo del empleado.
     * @param contraseña La contraseña del empleado.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean validarEmpleado(String correo, String contraseña) {
        boolean esValido = false;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT * FROM EMPLEADO WHERE email = ? AND contraseña = ?";
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
     * Obtiene un empleado a partir de su ID.
     * 
     * @param id El ID del empleado.
     * @return El empleado correspondiente al ID, o null si no existe.
     */
    @Override
    public Empleado obtenerPorId(int id) {
        String sql = "SELECT * FROM Empleado WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
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
     * Obtiene todos los empleados de la base de datos.
     * 
     * @return Una lista de todos los empleados.
     */
    @Override
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM Empleado";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }

    /**
     * Actualiza los datos de un empleado en la base de datos.
     * 
     * @param id              El ID del empleado a actualizar.
     * @param empleadoActualizado El empleado con los nuevos datos.
     */
    @Override
    public void actualizar(int id, Empleado empleadoActualizado) {
        String sql = "UPDATE Empleado SET nombre = ?, email = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, empleadoActualizado.getNombre());
            pstmt.setString(2, empleadoActualizado.getCorreo());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un empleado de la base de datos.
     * 
     * @param id El ID del empleado a eliminar.
     */
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Empleado WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID de un empleado a partir de su correo electrónico.
     * 
     * @param email El correo electrónico del empleado.
     * @return El ID del empleado correspondiente, o -1 si no se encuentra.
     */
    public int obtenerId(String email) {
        String sql = "SELECT id FROM Empleado WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");  // Return the ID of the employee with the given email
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if no employee is found with the given email
    }

    /**
     * Obtiene un empleado a partir de su correo electrónico.
     * 
     * @param correo El correo electrónico del empleado.
     * @return El empleado correspondiente al correo, o null si no existe.
     */
    public Empleado obtenerPorCorreo(String correo) {
        String sql = "SELECT * FROM Empleado WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
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
     * Elimina todos los empleados de la base de datos.
     */
    public void eliminarTodos() {
        String sql = "DELETE FROM Empleado";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
