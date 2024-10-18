package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Conexion;

/**
 * Interfaz que define los métodos para gestionar las conexiones.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para las conexiones.
 */
public interface ConexionDAO {

    /**
     * Inserta una nueva conexión en el sistema.
     * @param conexion La conexión a insertar.
     */
    void insertar(Conexion conexion);

    /**
     * Actualiza una conexión existente en el sistema.
     * @param conexion La conexión a actualizar.
     */
    void actualizar(Conexion conexion);

    /**
     * Borra una conexión existente en el sistema.
     * @param conexion La conexión a borrar.
     */
    void borrar(Conexion conexion);

    /**
     * Busca y retorna todas las conexiones del sistema.
     * @return Lista de todas las conexiones.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<Conexion> buscarTodos() throws FileNotFoundException;
}
