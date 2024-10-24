package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Equipo;

/**
 * Interfaz para el servicio de gestión de equipos.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre los equipos.
 */
public interface EquipoService {

    /**
     * Inserta un nuevo equipo en el sistema.
     * 
     * @param equipo El equipo a insertar.
     */
    void insertar(Equipo equipo);

    /**
     * Actualiza la información de un equipo existente.
     * 
     * @param equipo El equipo a actualizar.
     */
    void actualizar(Equipo equipo);

    /**
     * Elimina un equipo del sistema.
     * 
     * @param equipo El equipo a eliminar.
     */
    void borrar(Equipo equipo);

    /**
     * Busca y retorna todos los equipos almacenados.
     * 
     * @return Lista de todos los equipos.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<Equipo> buscarTodos() throws FileNotFoundException;

    // Método para buscar equipo por código
    Equipo buscarPorCodigo(String codigo) throws FileNotFoundException;
}
