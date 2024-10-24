package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Ubicacion;

/**
 * Interfaz para el servicio de gestión de ubicaciones.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre las ubicaciones.
 */
public interface UbicacionService {

    /**
     * Inserta una nueva ubicación en el sistema.
     * 
     * @param ubicacion La ubicación a insertar.
     */
    void insertar(Ubicacion ubicacion);

    /**
     * Actualiza la información de una ubicación existente.
     * 
     * @param ubicacion La ubicación a actualizar.
     */
    void actualizar(Ubicacion ubicacion);

    /**
     * Elimina una ubicación del sistema.
     * 
     * @param ubicacion La ubicación a eliminar.
     */
    void borrar(Ubicacion ubicacion);

    /**
     * Busca y retorna todas las ubicaciones almacenadas.
     * 
     * @return Lista de todas las ubicaciones.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<Ubicacion> buscarTodos() throws FileNotFoundException;
}
