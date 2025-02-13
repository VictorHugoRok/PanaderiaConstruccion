package Modelo;

import java.util.List;

/**
 * Interfaz que define las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para las entidades que implementen esta interfaz. 
 * Las operaciones están diseñadas para trabajar con cualquier tipo de entidad, representada por el tipo genérico {@link T}.
 * 
 * @param <T> El tipo de objeto sobre el que se realizan las operaciones.
 */
public interface OperacionesDAO<T> {

    /**
     * Agrega un nuevo objeto a la base de datos.
     * 
     * @param objeto El objeto a agregar.
     * @return El ID del objeto recién agregado.
     */
    int agregar(T objeto);

    /**
     * Obtiene un objeto a partir de su ID.
     * 
     * @param id El ID del objeto a obtener.
     * @return El objeto correspondiente al ID, o null si no se encuentra.
     */
    T obtenerPorId(int id);

    /**
     * Obtiene todos los objetos de la base de datos.
     * 
     * @return Una lista con todos los objetos.
     */
    List<T> obtenerTodos();

    /**
     * Actualiza los datos de un objeto en la base de datos.
     * 
     * @param id El ID del objeto a actualizar.
     * @param objetoActualizado El objeto con los datos actualizados.
     */
    void actualizar(int id, T objetoActualizado);

    /**
     * Elimina un objeto de la base de datos.
     * 
     * @param id El ID del objeto a eliminar.
     */
    void eliminar(int id);
}
