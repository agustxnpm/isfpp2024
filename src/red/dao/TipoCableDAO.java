package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoCable;

/**
 * Interfaz que define los métodos para gestionar los tipos de cables.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para los tipos de cables.
 */
public interface TipoCableDAO {

    /**
     * Inserta un nuevo tipo de cable en el sistema.
     * @param tipoCable El tipo de cable a insertar.
     */
    void insertar(TipoCable tipoCable);

    /**
     * Actualiza un tipo de cable existente en el sistema.
     * @param tipoCable El tipo de cable a actualizar.
     */
    void actualizar(TipoCable tipoCable);

    /**
     * Borra un tipo de cable existente en el sistema.
     * @param tipoCable El tipo de cable a borrar.
     */
    void borrar(TipoCable tipoCable);

    /**
     * Busca y retorna todos los tipos de cables del sistema.
     * @return Lista de todos los tipos de cables.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<TipoCable> buscarTodos() throws FileNotFoundException;
}
