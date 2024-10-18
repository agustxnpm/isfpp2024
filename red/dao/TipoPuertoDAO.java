package red.dao;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoPuerto;

/**
 * Interfaz que define los métodos para gestionar los tipos de puertos.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Borrar) para los tipos de puertos.
 */
public interface TipoPuertoDAO {

    /**
     * Inserta un nuevo tipo de puerto en el sistema.
     * @param tipoPuerto El tipo de puerto a insertar.
     */
    void insertar(TipoPuerto tipoPuerto);

    /**
     * Actualiza un tipo de puerto existente en el sistema.
     * @param tipoPuerto El tipo de puerto a actualizar.
     */
    void actualizar(TipoPuerto tipoPuerto);

    /**
     * Borra un tipo de puerto existente en el sistema.
     * @param tipoPuerto El tipo de puerto a borrar.
     */
    void borrar(TipoPuerto tipoPuerto);

    /**
     * Busca y retorna todos los tipos de puertos del sistema.
     * @return Lista de todos los tipos de puertos.
     * @throws FileNotFoundException Si el archivo de datos no se encuentra.
     */
    List<TipoPuerto> buscarTodos() throws FileNotFoundException;
}
