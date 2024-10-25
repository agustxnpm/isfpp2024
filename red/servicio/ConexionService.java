package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Conexion;

/**
 * Interfaz para el servicio de gestión de conexiones.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre las conexiones.
 */
public interface ConexionService {

    /**
     * Inserta una nueva conexión en el sistema.
     * 
     * @param conexion La conexión a insertar.
     */
    void insertar(Conexion conexion);

    /**
     * Actualiza la información de una conexión existente.
     * 
     * @param conexion La conexión a actualizar.
     */
    void actualizar(Conexion conexion);

    /**
     * Elimina una conexión del sistema.
     * 
     * @param conexion La conexión a eliminar.
     */
    void borrar(Conexion conexion);

    /**
     * Busca y retorna todas las conexiones almacenadas.
     * 
     * @return Lista de todas las conexiones.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<Conexion> buscarTodos() throws FileNotFoundException;

    Conexion buscarPorCodigo(String equipo1Codigo, String equipo2Codigo) throws FileNotFoundException;


}


