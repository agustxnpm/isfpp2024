package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.Equipo;

/**
 * Interfaz que define los métodos para gestionar los equipos.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para los equipos.
 */
public interface EquipoDAO {

    /**
     * Inserta un nuevo equipo en el sistema.
     * @param equipo El equipo a insertar.
     */
    void insertar(Equipo equipo);

    /**
     * Actualiza un equipo existente en el sistema.
     * @param equipo El equipo a actualizar.
     */
    void actualizar(Equipo equipo);

    /**
     * Borra un equipo existente en el sistema.
     * @param equipo El equipo a borrar.
     */
    void borrar(Equipo equipo);

    /**
     * Busca y retorna todos los equipos del sistema.
     * @return Lista de todos los equipos.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<Equipo> buscarTodos() throws FileNotFoundException;
}
