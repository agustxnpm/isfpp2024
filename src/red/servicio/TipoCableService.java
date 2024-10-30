package red.servicio;

import java.io.FileNotFoundException;
import java.util.List;
import red.modelo.TipoCable;

/**
 * Interfaz para el servicio de gestión de tipos de cable.
 * Define los métodos que deben implementarse para manejar las operaciones CRUD sobre los tipos de cable.
 */
public interface TipoCableService {

    /**
     * Inserta un nuevo tipo de cable en el sistema.
     * 
     * @param tipoCable El tipo de cable a insertar.
     */
    void insertar(TipoCable tipoCable);

    /**
     * Actualiza la información de un tipo de cable existente.
     * 
     * @param tipoCable El tipo de cable a actualizar.
     */
    void actualizar(TipoCable tipoCable);

    /**
     * Elimina un tipo de cable del sistema.
     * 
     * @param tipoCable El tipo de cable a eliminar.
     */
    void borrar(TipoCable tipoCable);

    /**
     * Busca y retorna todos los tipos de cable almacenados.
     * 
     * @return Lista de todos los tipos de cable.
     * @throws FileNotFoundException Si no se puede acceder a la fuente de datos.
     */
    List<TipoCable> buscarTodos() throws FileNotFoundException;
}
