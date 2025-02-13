package Controller;

import Modelo.AdministradorDAO;
import Modelo.EmpleadoDAO;

/**
 * Clase LoginControlador.
 * Controlador que maneja el inicio de sesión para administradores y empleados,
 * validando sus credenciales a través de los DAOs correspondientes.
 */
public class LoginControlador {

    private AdministradorDAO administradorDAO;
    private EmpleadoDAO empleadoDAO;

    /**
     * Constructor que inicializa los DAOs para administradores y empleados.
     */
    public LoginControlador() {
        this.administradorDAO = new AdministradorDAO();
        this.empleadoDAO = new EmpleadoDAO();
    }

    /**
     * Método para manejar el inicio de sesión.
     * Valida las credenciales de un usuario (administrador o empleado).
     *
     * @param correo      El correo electrónico del usuario.
     * @param contraseña  La contraseña del usuario.
     * @return Una cadena que indica el tipo de usuario:
     *         - "administrador" si las credenciales pertenecen a un administrador.
     *         - "empleado" si las credenciales pertenecen a un empleado.
     *         - "invalido" si las credenciales no son válidas para ningún usuario.
     */
    public String login(String correo, String contraseña) {
        if (administradorDAO.validarAdministrador(correo, contraseña)) {
            return "administrador"; // Si es un administrador válido
        }

        // Si no es Administrador, intentar validar como Empleado
        if (empleadoDAO.validarEmpleado(correo, contraseña)) {
            return "empleado"; // Si es un empleado válido
        }

        // Si no se encuentra en ninguna tabla, retorno "invalido"
        return "invalido";
    }
}
