package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoEquipo;

/**
 * Interfaz para el servicio de gestión de tipos de equipo.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre los tipos de equipo.
 */
public interface TipoEquipoService {

    /**
     * Inserta un nuevo tipo de equipo en el sistema.
     * 
     * @param tipoEquipo El tipo de equipo a insertar.
     */
    void insertar(TipoEquipo tipoEquipo);

    /**
     * Actualiza la información de un tipo de equipo existente.
     * 
     * @param tipoEquipo El tipo de equipo a actualizar.
     */
    void actualizar(TipoEquipo tipoEquipo);

    /**
     * Elimina un tipo de equipo del sistema.
     * 
     * @param tipoEquipo El tipo de equipo a eliminar.
     */
    void borrar(TipoEquipo tipoEquipo);

    /**
     * Busca y retorna todos los tipos de equipo almacenados.
     * 
     * @return Lista de todos los tipos de equipo.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<TipoEquipo> buscarTodos() throws FileNotFoundException;
}
