package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Ubicacion;

/**
 * Interfaz que define los métodos para gestionar las ubicaciones.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para las ubicaciones.
 */
public interface UbicacionDAO {

    /**
     * Inserta una nueva ubicación en el sistema.
     * @param ubicacion La ubicación a insertar.
     */
    void insertar(Ubicacion ubicacion);

    /**
     * Actualiza una ubicación existente en el sistema.
     * @param ubicacion La ubicación a actualizar.
     */
    void actualizar(Ubicacion ubicacion);

    /**
     * Borra una ubicación existente en el sistema.
     * @param ubicacion La ubicación a borrar.
     */
    void borrar(Ubicacion ubicacion);

    /**
     * Busca y retorna todas las ubicaciones del sistema.
     * @return Lista de todas las ubicaciones.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<Ubicacion> buscarTodos() throws FileNotFoundException;
}
