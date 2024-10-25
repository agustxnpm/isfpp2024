package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoEquipo;

/**
 * Interfaz que define los métodos para gestionar los tipos de equipos.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para los tipos de equipos.
 */
public interface TipoEquipoDAO {

    /**
     * Inserta un nuevo tipo de equipo en el sistema.
     * @param tipoEquipo El tipo de equipo a insertar.
     */
    void insertar(TipoEquipo tipoEquipo);

    /**
     * Actualiza un tipo de equipo existente en el sistema.
     * @param tipoEquipo El tipo de equipo a actualizar.
     */
    void actualizar(TipoEquipo tipoEquipo);

    /**
     * Borra un tipo de equipo existente en el sistema.
     * @param tipoEquipo El tipo de equipo a borrar.
     */
    void borrar(TipoEquipo tipoEquipo);

    /**
     * Busca y retorna todos los tipos de equipos del sistema.
     * @return Lista de todos los tipos de equipos.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<TipoEquipo> buscarTodos() throws FileNotFoundException;
}
