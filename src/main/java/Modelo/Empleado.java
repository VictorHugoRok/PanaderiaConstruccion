package Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Empleado que representa a un empleado en el sistema.
 * Un empleado tiene pedidos asignados y puede gestionarlos (iniciar, pausar, finalizar).
 * Hereda de la clase {@link Usuario}.
 */
public class Empleado extends Usuario {
    
    private List<Pedido> pedidosAsignados;
    private PedidoDAO pedidoDAO;

    /**
     * Constructor de la clase Empleado.
     * 
     * @param id     El ID del empleado.
     * @param nombre El nombre del empleado.
     * @param correo El correo electrónico del empleado.
     */
    public Empleado(int id, String nombre, String correo) {
        super(id, nombre, correo);
        this.pedidoDAO = new PedidoDAO();
        this.pedidosAsignados = new ArrayList<>();
    }

    /**
     * Constructor de la clase Empleado con nombre, correo y contraseña.
     * 
     * @param nombre     El nombre del empleado.
     * @param correo     El correo electrónico del empleado.
     * @param contraseña La contraseña del empleado.
     */
    public Empleado(String nombre, String correo, String contraseña) {
        super(nombre, correo, contraseña);
    }

    /**
     * Obtiene todos los pedidos asignados al empleado.
     * Si la lista de pedidos asignados está vacía, se consulta la base de datos
     * para obtener todos los pedidos asignados al empleado actual.
     * 
     * @return Una lista de pedidos asignados al empleado.
     */
    public List<Pedido> verPedidos() {
        if (pedidosAsignados == null || pedidosAsignados.isEmpty()) {
            pedidosAsignados = pedidoDAO.obtenerTodos()
                    .stream()
                    .filter(p -> p.getEmpleadoAsignado().getId() == this.getId())
                    .toList();
        }
        return pedidosAsignados;
    }

    /**
     * Inicia el trabajo en un pedido asignado al empleado.
     * Verifica si el pedido está asignado al empleado y si existe en la base de datos.
     * 
     * @param idPedido El ID del pedido en el cual se va a iniciar el trabajo.
     */
    public void iniciarTrabajoEnPedido(int idPedido) {
        Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }
        if (pedido.getEmpleadoAsignado().getId() != this.getId()) {
            System.out.println("El pedido no está asignado a este empleado.");
            return;
        }
        System.out.println("Trabajo iniciado en el pedido ID: " + idPedido);
        // Aquí podrías agregar lógica para registrar el inicio de trabajo en la base de datos.
    }

    /**
     * Finaliza un pedido asignado al empleado.
     * Verifica si el pedido está asignado al empleado y si existe en la base de datos.
     * 
     * @param idPedido El ID del pedido que se va a finalizar.
     */
    public void finalizarPedido(int idPedido) {
        Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }
        if (pedido.getEmpleadoAsignado().getId() != this.getId()) {
            System.out.println("El pedido no está asignado a este empleado.");
            return;
        }
        System.out.println("Pedido finalizado: ID " + idPedido);
        // Aquí podrías agregar lógica para marcar el pedido como finalizado en la base de datos.
    }

    /**
     * Pausa un pedido asignado al empleado.
     * Verifica si el pedido está asignado al empleado y si existe en la base de datos.
     * 
     * @param idPedido El ID del pedido que se va a pausar.
     */
    public void pausarPedido(int idPedido) {
        Pedido pedido = pedidoDAO.obtenerPorId(idPedido);
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }
        if (pedido.getEmpleadoAsignado().getId() != this.getId()) {
            System.out.println("El pedido no está asignado a este empleado.");
            return;
        }
        System.out.println("Pedido pausado: ID " + idPedido);
        // Aquí podrías agregar lógica para registrar la pausa en la base de datos.
    }
}
